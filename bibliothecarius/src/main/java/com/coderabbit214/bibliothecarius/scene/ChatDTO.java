package com.coderabbit214.bibliothecarius.scene;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Mr_J
 * @Date: 2023/3/19 12:02
 */
@Setter
@Getter
@ToString
public class ChatDTO {
    /**
     * 上下文id
     */
    private String contextId;
    private String context;
}
