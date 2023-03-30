package com.coderabbit214.bibliothecarius.scene;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.common.utils.JsonUtil;
import com.coderabbit214.bibliothecarius.common.utils.SpringUtil;
import com.coderabbit214.bibliothecarius.dataset.Dataset;
import com.coderabbit214.bibliothecarius.dataset.DatasetService;
import com.coderabbit214.bibliothecarius.externalModel.ExternalModelService;
import com.coderabbit214.bibliothecarius.model.ModelFactory;
import com.coderabbit214.bibliothecarius.model.ModelInterface;
import com.coderabbit214.bibliothecarius.qdrant.QdrantService;
import com.coderabbit214.bibliothecarius.qdrant.point.PointSearchParams;
import com.coderabbit214.bibliothecarius.qdrant.point.PointSearchRequest;
import com.coderabbit214.bibliothecarius.qdrant.point.PointSearchResponse;
import com.coderabbit214.bibliothecarius.scene.context.ChatContext;
import com.coderabbit214.bibliothecarius.scene.context.ChatContextService;
import com.coderabbit214.bibliothecarius.vector.VectorInterface;
import com.coderabbit214.bibliothecarius.vector.VectorFactory;
import com.coderabbit214.bibliothecarius.vector.VectorResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 情景 服务实现类
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-18
 */
@Service
public class SceneService extends ServiceImpl<SceneMapper, Scene> {

    private final ModelFactory modelFactory;

    private final VectorFactory vectorFactory;

    private final DatasetService datasetService;

    private final ChatContextService chatContextService;

    private final ExternalModelService externalModelService;

    public SceneService(ModelFactory modelFactory, VectorFactory vectorFactory, DatasetService datasetService, ChatContextService chatContextService, ExternalModelService externalModelService) {
        this.modelFactory = modelFactory;
        this.vectorFactory = vectorFactory;
        this.datasetService = datasetService;
        this.chatContextService = chatContextService;
        this.externalModelService = externalModelService;
    }

    /**
     * 添加检测
     *
     * @param scene
     */
    public void check(Scene scene) {
        if (!scene.getName().matches("^[a-zA-Z_]+$")) {
            throw new BusinessException("name must be in english");
        }
        //名称重复
        if (checkName(scene.getId(), scene.getName())) {
            throw new BusinessException("duplicate name");
        }
        //检查消息模版是否含有 ${message}
        String template = scene.getTemplate();
        if (!template.contains("${message}")) {
            throw new BusinessException("Message Template must include `${message}` placeholder");
        }
        //检查模型类型
        String modelType = scene.getModelType();
        List<String> modelTypes = this.getModelType();
        if (!modelTypes.contains(modelType)) {
            throw new BusinessException("model type does not exist");
        }
        //模型参数检查
        ModelInterface modelService = modelFactory.getModelService(modelType);
        modelService.checkParams(scene);
    }

    public boolean checkName(Long id, String name) {
        LambdaQueryWrapper<Scene> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(id != null, Scene::getId, id);
        queryWrapper.eq(Scene::getName, name);
        return this.count(queryWrapper) > 0;
    }

    public void update(SceneUpdateDTO sceneUpdateDTO) {
        Scene scene = this.getById(sceneUpdateDTO.getId());
        SpringUtil.copyPropertiesIgnoreNull(sceneUpdateDTO, scene);
        if (sceneUpdateDTO.getParams() != null) {
            scene.setParams(JsonUtil.toJsonString(sceneUpdateDTO.getParams()));
        }
        this.check(scene);
        this.updateById(scene);
    }

    public void add(SceneAddDTO sceneAddDTO) {
        Scene scene = new Scene();
        BeanUtils.copyProperties(sceneAddDTO, scene);
        scene.setParams(JsonUtil.toJsonString(sceneAddDTO.getParams()));
        this.check(scene);
        this.save(scene);
    }

    public Scene getByName(String name) {
        LambdaQueryWrapper<Scene> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Scene::getName, name);
        return this.getOne(queryWrapper);
    }

    /**
     * chat
     *
     * @param name
     * @param chatDTO
     * @return
     */
    public ChatResult chat(String name, ChatDTO chatDTO) {
        Scene scene = this.getByName(name);
        if (scene == null) {
            throw new BusinessException("scene does not exist");
        }
        List<ChatContext> chatContextList = new ArrayList<>();
        if (chatDTO.getContextId() != null) {
            chatContextList = chatContextService.listByIdLimit(chatDTO.getContextId(), chatDTO.getHistorySize());
        }
        ChatResult chatResult = new ChatResult();
        List<Object> jsonData = new ArrayList<>();

        List<String> relevantDataList = new ArrayList<>();

        if (scene.getDatasetId() != null) {
            VectorInterface vectorService = vectorFactory.getVectorService(VectorInterface.TYPE_OPENAI_VECTOR);
            List<VectorResult> vectorResultList = vectorService.getVector(chatDTO.getContext());
            List<Double> vector = vectorResultList.get(0).getVector();
            QdrantService qdrantService = new QdrantService();
            Dataset dataset = datasetService.getById(scene.getDatasetId());
            PointSearchRequest pointSearchRequest = new PointSearchRequest();
            pointSearchRequest.setLimit(dataset.getRelevantSize());
            pointSearchRequest.setVector(vector);
            pointSearchRequest.setWithPayload(true);
            PointSearchParams params = new PointSearchParams();
            params.setExact(false);
            params.setHnswEf(128);
            pointSearchRequest.setParams(params);
            List<PointSearchResponse> pointSearchResponses = qdrantService.searchPoints(dataset.getName(), pointSearchRequest);
            for (int i = 0; i < pointSearchResponses.size(); i++) {
                PointSearchResponse pointSearchResponse = pointSearchResponses.get(i);
                jsonData.add(pointSearchResponse.getPayload());
                JsonNode jsonNode = JsonUtil.toObject(JsonUtil.toJsonString(pointSearchResponse.getPayload()), JsonNode.class);
                String content = jsonNode.get("context").asText();
                relevantDataList.add(content);
            }
        }
        chatResult.setJsonData(jsonData);
        ModelInterface modelService = modelFactory.getModelService(scene.getModelType());

        List<String> contexts = modelService.chat(scene, chatDTO.getContext(), relevantDataList, chatContextList);

        chatResult.setContents(contexts);

        ChatContext chatContext = new ChatContext();
        chatContext.setUser(chatDTO.getContext());
        StringBuilder assistant = new StringBuilder();
        for (String c : contexts) {
            assistant.append(c);
        }
        chatContext.setAssistant(assistant.toString());
        if (chatContextList.size() == 0) {
            String id = UUID.randomUUID().toString();
            chatContext.setId(id);
            chatContext.setSort(1);
        } else {
            chatContext.setId(chatContextList.get(0).getId());
            chatContext.setSort(chatContextList.size() + 1);
        }

        chatContextService.save(chatContext);
        chatResult.setContextId(chatContext.getId());
        return chatResult;
    }

    public Long countByModelType(String modelType) {
        LambdaQueryWrapper<Scene> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Scene::getModelType, modelType);
        return this.count(queryWrapper);
    }

    public List<String> getModelType() {
        List<String> names = externalModelService.getNameList();
        List<String> modelTypes = new ArrayList<>(ModelInterface.MODEL_TYPES);
        modelTypes.addAll(names);
        return modelTypes;
    }
}
