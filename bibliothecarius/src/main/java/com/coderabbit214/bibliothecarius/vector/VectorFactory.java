package com.coderabbit214.bibliothecarius.vector;

import com.coderabbit214.bibliothecarius.vector.openai.OpenAiVectorService;
import org.springframework.stereotype.Component;

/**
 * 向量计算方式工厂
 * @author Mr_J
 */
@Component
public class VectorFactory {

    private final OpenAiVectorService openAiVectorService;

    public VectorFactory(OpenAiVectorService openAiVectorService) {
        this.openAiVectorService = openAiVectorService;
    }

    public VectorInterface getVectorService(String vectorType) {
        if (vectorType.equals(VectorInterface.TYPE_OPENAI_VECTOR)) {
            return openAiVectorService;
        } else {
            throw new IllegalArgumentException("vector calculation method does not exist");
        }
    }
}
