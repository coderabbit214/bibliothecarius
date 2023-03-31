package com.coderabbit214.bibliothecarius.vector;

import com.coderabbit214.bibliothecarius.externalVector.ExternalVectorService;
import com.coderabbit214.bibliothecarius.vector.external.VectorExternalService;
import com.coderabbit214.bibliothecarius.vector.openai.OpenAiVectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 向量计算方式工厂
 *
 * @author Mr_J
 */
@Component
public class VectorFactory {

    private final OpenAiVectorService openAiVectorService;

    private final VectorExternalService vectorExternalService;

    private final ExternalVectorService externalVectorService;

    public VectorFactory(OpenAiVectorService openAiVectorService, VectorExternalService vectorExternalService, ExternalVectorService externalVectorService) {
        this.openAiVectorService = openAiVectorService;
        this.vectorExternalService = vectorExternalService;
        this.externalVectorService = externalVectorService;
    }

    public VectorInterface getVectorService(String vectorType) {
        if (vectorType.equals(VectorInterface.TYPE_OPENAI_VECTOR)) {
            return openAiVectorService;
        }
        List<String> nameList = externalVectorService.getNameList();
        if (nameList.contains(vectorType)) {
            return vectorExternalService;
        }
        throw new IllegalArgumentException("vector calculation method does not exist");
    }
}
