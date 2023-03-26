package com.coderabbit214.bibliothecarius.openai.chat;

import lombok.Data;

@Data
public class ChatChoice {

    /**
     * This index of this completion in the returned list.
     */
    Integer index;

    /**
     * The {@link ChatMessageRole#assistant} message which was generated.
     */
    ChatMessage message;

    /**
     * The reason why GPT-3 stopped generating, for example "length".
     */
    String finishReason;
}
