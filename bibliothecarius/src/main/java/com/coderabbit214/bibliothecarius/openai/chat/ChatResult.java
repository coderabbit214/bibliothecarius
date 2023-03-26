package com.coderabbit214.bibliothecarius.openai.chat;

import com.coderabbit214.bibliothecarius.openai.Usage;
import lombok.Data;

import java.util.List;

@Data
public class ChatResult {

    /**
     * Unique id assigned to this chat completion.
     */
    String id;

    /**
     * The type of object returned, should be "chat.completion"
     */
    String object;

    /**
     * The creation time in epoch seconds.
     */
    long created;
    
    /**
     * The GPT-3.5 model used.
     */
    String model;

    /**
     * A list of all generated completions.
     */
    List<ChatChoice> choices;

    /**
     * The API usage for this request.
     */
    Usage usage;

}
