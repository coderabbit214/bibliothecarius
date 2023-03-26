package com.coderabbit214.bibliothecarius.model;

import com.coderabbit214.bibliothecarius.model.openai.OpenAiChatService;
import org.springframework.stereotype.Component;

/**
 * 模型工厂
 */
@Component
public class ModelFactory {

    private final OpenAiChatService openAiChatService;

    public ModelFactory(OpenAiChatService openAiChatService) {
        this.openAiChatService = openAiChatService;
    }

    public ModelInterface getModelService(String modelType) {
        if (modelType.equals(ModelInterface.TYPE_OPENAI_CHAT)) {
            return openAiChatService;
        } else {
            throw new IllegalArgumentException("model type does not exist");
        }
    }
}
