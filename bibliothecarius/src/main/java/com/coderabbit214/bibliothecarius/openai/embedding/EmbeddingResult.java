package com.coderabbit214.bibliothecarius.openai.embedding;

import com.coderabbit214.bibliothecarius.openai.Usage;
import lombok.Data;

import java.util.List;

@Data
public class EmbeddingResult {

    /**
     * The GPT-3 model used for generating embeddings
     */
    String model;

    /**
     * The type of object returned, should be "list"
     */
    String object;

    /**
     * A list of the calculated embeddings
     */
    List<Embedding> data;

    /**
     * The API usage for this request
     */
    Usage usage;
}
