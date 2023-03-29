package com.coderabbit214.bibliothecarius.model.openai;

import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.common.utils.JsonUtil;
import com.coderabbit214.bibliothecarius.model.ModelInterface;
import com.coderabbit214.bibliothecarius.openai.OpenAiService;
import com.coderabbit214.bibliothecarius.openai.OpenAiUtil;
import com.coderabbit214.bibliothecarius.openai.chat.*;
import com.coderabbit214.bibliothecarius.scene.Scene;
import com.coderabbit214.bibliothecarius.scene.context.ChatContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr_J
 */
@Service
public class OpenAiChatService implements ModelInterface {

    @Value("${openai.api-key}")
    private String apiKey;

    @Override
    public void checkParams(String params) {
        ChatRequest chatRequest = JsonUtil.toObject(params, ChatRequest.class);
        String model = chatRequest.getModel();
        if (!ChatRequest.MODEL_TYPES.contains(model)) {
            throw new BusinessException("openai model type does not exist");
        }
    }

    @Override
    public List<String> chat(Scene scene, String input, String data, List<ChatContext> chatContextList) {
        List<String> result = new ArrayList<>();
        ChatRequest chatRequest = JsonUtil.toObject(scene.getParams(), ChatRequest.class);

        //补充上下文
        for (int i = chatContextList.size() - 1; i >= 0 && i > chatContextList.size() - 3; i--) {
            ChatMessage chatMessage = new ChatMessage();
            ChatContext chatContext = chatContextList.get(i);
            chatMessage.setContent(chatContext.getUser());
            chatMessage.setRole(ChatMessageRole.USER.value());
            chatRequest.getMessages().add(chatMessage);
            chatMessage = new ChatMessage();
            chatMessage.setContent(chatContext.getAssistant());
            chatMessage.setRole(ChatMessageRole.ASSISTANT.value());
            chatRequest.getMessages().add(chatMessage);
        }

        ChatMessage chatMessage = new ChatMessage();
        String template = scene.getTemplate();
        template = template.replace("${message}", input);
        template = template.replace("${data}", data);
        chatMessage.setContent(template);
        chatMessage.setRole(ChatMessageRole.USER.value());
        chatRequest.getMessages().add(chatMessage);
        Integer maxTokens = chatRequest.getMaxTokens();
        if (maxTokens != null) {
            chatRequest.setMaxTokens(maxTokens - OpenAiUtil.getTokens(template));
        }
        List<ChatMessage> messages = chatRequest.getMessages();
        messages.forEach(message -> {
            String messageContent = message.getContent();
            if (messageContent != null) {
                message.setContent(messageContent.replace("${data}", data));
            }
        });
        OpenAiService openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
        ChatResult chatCompletion = openAiService.createChatCompletion(chatRequest);
        List<ChatChoice> choices = chatCompletion.getChoices();
        for (ChatChoice choice : choices) {
            result.add(choice.getMessage().getContent());
        }
        return result;
    }
}
