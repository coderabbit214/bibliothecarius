package com.coderabbit214.bibliothecarius.dataset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.dataset.document.DocumentService;
import com.coderabbit214.bibliothecarius.externalVector.ExternalVector;
import com.coderabbit214.bibliothecarius.externalVector.ExternalVectorService;
import com.coderabbit214.bibliothecarius.model.ModelInterface;
import com.coderabbit214.bibliothecarius.qdrant.QdrantService;
import com.coderabbit214.bibliothecarius.qdrant.collection.CollectionRequest;
import com.coderabbit214.bibliothecarius.qdrant.collection.Vectors;
import com.coderabbit214.bibliothecarius.qdrant.collection.VectorsDistance;
import com.coderabbit214.bibliothecarius.qdrant.point.Point;
import com.coderabbit214.bibliothecarius.qdrant.point.PointCreateRequest;
import com.coderabbit214.bibliothecarius.vector.VectorFactory;
import com.coderabbit214.bibliothecarius.vector.VectorInterface;
import com.coderabbit214.bibliothecarius.vector.VectorResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 数据集 服务实现类
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-18
 */
@Service
public class DatasetService extends ServiceImpl<DatasetMapper, Dataset> {

    private final VectorFactory vectorFactory;

    private final JsonQdrantService jsonQdrantService;

    private final DocumentService documentService;

    private final ExternalVectorService externalVectorService;

    public DatasetService(VectorFactory vectorFactory, JsonQdrantService jsonQdrantService, DocumentService documentService, ExternalVectorService externalVectorService) {
        this.vectorFactory = vectorFactory;
        this.jsonQdrantService = jsonQdrantService;
        this.documentService = documentService;
        this.externalVectorService = externalVectorService;
    }

    public boolean checkName(Long id, String name) {
        if (!name.matches("^[a-zA-Z_]+$")) {
            throw new BusinessException("name must be in english");
        }
        LambdaQueryWrapper<Dataset> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(id != null, Dataset::getId, id);
        queryWrapper.eq(Dataset::getName, name);
        return this.count(queryWrapper) > 0;
    }


    public void update(Dataset dataset) {
        //暂时不支持修改名称
        if (dataset.getName() != null) {
            throw new BusinessException("name modification is not supported");
        }
        this.updateById(dataset);
    }

    @Transactional(rollbackFor = Exception.class)
    public Dataset add(Dataset dataset) {
        this.check(dataset);
        this.save(dataset);
        QdrantService qdrantService = new QdrantService();
        String vectorType = dataset.getVectorType();
        ExternalVector externalVector = externalVectorService.getByName(vectorType);
        CollectionRequest collectionRequest = new CollectionRequest();
        Vectors vectors = new Vectors();
        vectors.setDistance(VectorsDistance.COSINE.value());
        vectors.setSize(1536);
        if (externalVector != null) {
            vectors.setSize(externalVector.getSize());
        }
        collectionRequest.setVectors(vectors);
        qdrantService.createCollection(dataset.getName(), collectionRequest);
        return dataset;
    }

    /**
     * 添加检测
     *
     * @param dataset
     */
    public void check(Dataset dataset) {
        //名称重复
        if (checkName(dataset.getId(), dataset.getName())) {
            throw new BusinessException("duplicate name");
        }
        //检查类型
        List<String> vectorType = this.getVectorType();
        if (!vectorType.contains(dataset.getVectorType())) {
            throw new BusinessException("unsupported vector type");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Dataset dataset = this.getById(id);
        this.removeById(id);
        jsonQdrantService.deleteByDatasetId(dataset.getId());
        documentService.deleteByDatasetId(dataset.getId());
        QdrantService qdrantService = new QdrantService();
        qdrantService.deleteCollection(dataset.getName());
    }

    /**
     * 根据名称查询
     *
     * @param name
     * @return
     */
    public Dataset getByName(String name) {
        LambdaQueryWrapper<Dataset> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dataset::getName, name);
        return this.getOne(queryWrapper);
    }

    /**
     * json数据上传
     *
     * @param name
     * @param jsonNode
     */
    @Transactional(rollbackFor = Exception.class)
    public void uploadJson(String name, JsonNode jsonNode) {
        Dataset dataset = this.getByName(name);
        if (dataset == null) {
            throw new BusinessException("dataset does not exist");
        }
        //检查是否有context字段
        JsonNode contextJsonNode = jsonNode.get("context");
        if (contextJsonNode == null) {
            throw new BusinessException("The context field cannot be empty");
        }
        // jsonNode添加type
        ((ObjectNode) jsonNode).put("type", Dataset.DATA_TYPE_JSON);

        String context = contextJsonNode.toString();
        //openai 计算
        VectorInterface vectorService = vectorFactory.getVectorService(dataset.getVectorType());
        List<VectorResult> vectorResultList = vectorService.getVector(context, dataset.getVectorType());

        List<Point> pointList = new ArrayList<>();
        List<JsonQdrant> jsonQdrantList = new ArrayList<>();
        for (VectorResult vectorResult : vectorResultList) {
            Point point = new Point();
            String id = UUID.randomUUID().toString();
            point.setId(id);
            point.setVector(vectorResult.getVector());
            point.setPayload(jsonNode);
            pointList.add(point);
            JsonQdrant jsonQdrant = new JsonQdrant();
            jsonQdrant.setDatasetId(dataset.getId());
            jsonQdrant.setQdrantId(id);
            jsonQdrant.setInfo(jsonNode.toString());
            jsonQdrantList.add(jsonQdrant);
        }
        //添加到qdrant
        QdrantService qdrantService = new QdrantService();
        PointCreateRequest request = new PointCreateRequest();
        request.setPoints(pointList);
        qdrantService.createPoints(dataset.getName(), request);

        jsonQdrantService.saveBatch(jsonQdrantList);
    }


    public Long countByVectorType(String vectorType) {
        LambdaQueryWrapper<Dataset> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dataset::getVectorType, vectorType);
        return this.count(queryWrapper);
    }

    public List<String> getVectorType() {
        List<String> names = externalVectorService.getNameList();
        List<String> types = new ArrayList<>(VectorInterface.VECTOR_TYPES);
        types.addAll(names);
        return types;
    }
}
