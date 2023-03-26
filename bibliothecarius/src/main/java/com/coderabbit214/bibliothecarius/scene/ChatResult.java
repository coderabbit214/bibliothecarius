package com.coderabbit214.bibliothecarius.scene;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: Mr_J
 * @Date: 2023/3/19 12:08
 */
@Setter
@Getter
@ToString
public class ChatResult {

    private String contextId;

    private List<String> contents;

    private List<Object> jsonData;
}
