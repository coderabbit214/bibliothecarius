package com.coderabbit214.bibliothecarius.model.openai;

import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.common.utils.JsonUtil;
import com.coderabbit214.bibliothecarius.model.ModelInterface;
import com.coderabbit214.bibliothecarius.openai.OpenAiService;
import com.coderabbit214.bibliothecarius.common.utils.TokenUtil;
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

    /**
     * 请求数据最大值
     */
    public static final Integer MAX_TOKEN = 3000;

    @Override
    public void checkParams(Scene scene) {
        ChatRequest chatRequest = JsonUtil.toObject(scene.getParams(), ChatRequest.class);
        String model = chatRequest.getModel();
        if (!ChatRequest.MODEL_TYPES.contains(model)) {
            throw new BusinessException("openai model type does not exist");
        }
    }

    @Override
    public List<String> chat(Scene scene, String input, List<String> dataList, List<ChatContext> chatContextList) {
        List<String> result = new ArrayList<>();
        ChatRequest chatRequest = JsonUtil.toObject(scene.getParams(), ChatRequest.class);

        StringBuilder tokens = new StringBuilder();

        //前置计算
        List<ChatMessage> messages = chatRequest.getMessages();
        if (messages == null) {
            messages = new ArrayList<>();
            chatRequest.setMessages(messages);
        }
        if (messages.size() > 0) {
            for (ChatMessage message : messages) {
                tokens.append(message.getContent());
                tokens.append("\n");
            }
        }

        //上下文组装
        for (ChatContext chatContext : chatContextList) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setContent(chatContext.getUser());
            chatMessage.setRole(ChatMessageRole.USER.value());
            chatRequest.getMessages().add(chatMessage);
            chatMessage = new ChatMessage();
            chatMessage.setContent(chatContext.getAssistant());
            chatMessage.setRole(ChatMessageRole.ASSISTANT.value());
            chatRequest.getMessages().add(chatMessage);

            tokens.append(chatContext.getUser());
            tokens.append("\n");
            tokens.append(chatContext.getAssistant());
            tokens.append("\n");
        }

        //相关数据组装
        StringBuilder data = new StringBuilder();
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                if (TokenUtil.getTokens(tokens.toString()) > MAX_TOKEN) {
                    break;
                }
                data.append("\n");
                data.append(i + 1).append(".");
                data.append(dataList.get(i));
                tokens.append("\n");
                tokens.append(i + 1).append(".");
                tokens.append(dataList.get(i));
            }
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
            chatRequest.setMaxTokens(maxTokens - TokenUtil.getTokens(template));
        }
        OpenAiService openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
        ChatResult chatCompletion = openAiService.createChatCompletion(chatRequest);
        List<ChatChoice> choices = chatCompletion.getChoices();
        for (ChatChoice choice : choices) {
            result.add(choice.getMessage().getContent());
        }
        return result;
    }
}
