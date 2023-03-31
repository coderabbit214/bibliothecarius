package com.coderabbit214.bibliothecarius.vector;

import java.util.List;

/**
 * @Author: Mr_J
 * @Date: 2023/3/20 10:06
 */
public interface VectorInterface {

    String TYPE_OPENAI_VECTOR = "openaiVector";
    List<String> VECTOR_TYPES = List.of(TYPE_OPENAI_VECTOR);

    /**
     * 计算向量
     *
     * @param text
     * @return
     */
    List<VectorResult> getVector(String text, String vectorType);


}
