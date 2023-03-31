package com.coderabbit214.bibliothecarius.model;

import com.coderabbit214.bibliothecarius.externalModel.ExternalModelService;
import com.coderabbit214.bibliothecarius.model.external.ModelExternalService;
import com.coderabbit214.bibliothecarius.model.openai.OpenAiChatService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 模型工厂
 */
@Component
public class ModelFactory {

    private final ExternalModelService externalModelService;

    private final OpenAiChatService openAiChatService;

    private final ModelExternalService externalService;

    public ModelFactory(OpenAiChatService openAiChatService, ExternalModelService externalModelService, ModelExternalService externalService) {
        this.openAiChatService = openAiChatService;
        this.externalModelService = externalModelService;
        this.externalService = externalService;
    }

    public ModelInterface getModelService(String modelType) {
        if (modelType.equals(ModelInterface.TYPE_OPENAI_CHAT)) {
            return openAiChatService;
        }
        List<String> nameList = externalModelService.getNameList();
        if (nameList.contains(modelType)) {
            return externalService;
        }
        throw new IllegalArgumentException("model type does not exist");
    }
}
