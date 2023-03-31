package com.coderabbit214.bibliothecarius.model.external;

import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.common.utils.JsonUtil;
import com.coderabbit214.bibliothecarius.common.utils.TokenUtil;
import com.coderabbit214.bibliothecarius.externalModel.ExternalModel;
import com.coderabbit214.bibliothecarius.externalModel.ExternalModelService;
import com.coderabbit214.bibliothecarius.model.ModelInterface;
import com.coderabbit214.bibliothecarius.scene.Scene;
import com.coderabbit214.bibliothecarius.scene.context.ChatContext;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * @author Mr_J
 * @since 2023-03-29
 */
@Service
public class ModelExternalService implements ModelInterface {

    private final ExternalModelService externalModelService;

    public ModelExternalService(ExternalModelService externalModelService) {
        this.externalModelService = externalModelService;
    }

    @Override
    public void checkParams(Scene scene) {
        ExternalModel externalModel = externalModelService.getByName(scene.getModelType());
        if (externalModel == null) {
            throw new BusinessException("model type does not exist");
        }
        if (externalModel.getCheckParametersAddress() == null) {
            return;
        }
        //TODO:参数校验
    }

    @Override
    public List<String> chat(Scene scene, String input, List<String> dataList, List<ChatContext> chatContextList) {
        Object params = null;
        if (scene.getParams() != null) {
            params = JsonUtil.toObject(scene.getParams(), Object.class);
        }
        ExternalModel externalModel = externalModelService.getByName(scene.getModelType());
        if (externalModel == null) {
            throw new BusinessException("model type does not exist");
        }

        //组装数据
        ModelExternalRequest externalRequest = new ModelExternalRequest();
        externalRequest.setChatContextList(chatContextList);
        externalRequest.setParams(params);

        StringBuilder tokens = new StringBuilder();
        StringBuilder data = new StringBuilder();
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                if (TokenUtil.getTokens(tokens.toString()) > externalModel.getInputMaxToken()) {
                    break;
                }
                data.append("\n");
                data.append(i + 1).append(".");
                data.append(dataList.get(i));
                tokens.append("\n");
                tokens.append(i + 1).append(".");
                tokens.append(dataList.get(i));
            }
        }

        String template = scene.getTemplate();
        template = template.replace("${message}", input);
        template = template.replace("${data}", data);
        externalRequest.setInput(template);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JsonUtil.toJsonString(externalRequest);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(externalModel.getChatAddress(), request, String.class);
        if (stringResponseEntity.getStatusCode() != HttpStatus.OK) {
            throw new BusinessException("Abnormal service");
        }
        return Collections.singletonList(stringResponseEntity.getBody());
    }
}