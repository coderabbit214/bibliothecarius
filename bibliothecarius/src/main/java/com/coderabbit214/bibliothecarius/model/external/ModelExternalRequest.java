package com.coderabbit214.bibliothecarius.model.external;

import com.coderabbit214.bibliothecarius.scene.context.ChatContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class ModelExternalRequest {
    private Object params;
    private String input;
    private List<ChatContext> chatContextList;
}
