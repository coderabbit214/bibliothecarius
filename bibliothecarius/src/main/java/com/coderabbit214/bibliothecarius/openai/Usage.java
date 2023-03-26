package com.coderabbit214.bibliothecarius.openai;

import lombok.Data;

/**
 * The OpenAI resources used by a request
 */
@Data
public class Usage {
    /**
     * The number of prompt tokens used.
     */
    long promptTokens;

    /**
     * The number of completion tokens used.
     */
    long completionTokens;

    /**
     * The number of total tokens used
     */
    long totalTokens;
}
