package com.coderabbit214.bibliothecarius.model;

import com.coderabbit214.bibliothecarius.scene.Scene;
import com.coderabbit214.bibliothecarius.scene.context.ChatContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ModelInterface {
    String TEMPLATE = "${message}";
    String TYPE_OPENAI_CHAT = "openaiChat";
    List<String> MODEL_TYPES = List.of(TYPE_OPENAI_CHAT);

    /**
     * check params
     *
     * @param params
     */
    void checkParams(String params);

    /**
     * chat
     *
     * @param scene           场景
     * @param input           用户输入
     * @param dataList        相关数据
     * @param chatContextList 上下文数据
     * @return
     */
    List<String> chat(Scene scene, String input, List<String> dataList, List<ChatContext> chatContextList);
}
