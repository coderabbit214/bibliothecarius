package com.coderabbit214.bibliothecarius.openai.embedding;

import lombok.Data;

import java.util.List;

@Data
public class Embedding {

    /**
     * The type of object returned, should be "embedding"
     */
    String object;

    /**
     * The embedding vector
     */
    List<Double> embedding;

    /**
     * The position of this embedding in the list
     */
    Integer index;
}
