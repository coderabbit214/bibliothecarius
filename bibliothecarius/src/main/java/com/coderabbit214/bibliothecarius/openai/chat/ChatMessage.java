package com.coderabbit214.bibliothecarius.openai.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

	/**
	 * Must be either 'system', 'user', or 'assistant'.<br>
	 * You may use {@link ChatMessageRole} enum.
	 */
	String role;
	String content;
}
