package com.coderabbit214.bibliothecarius.dataset;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class JsonDTO {
    private String context;
    private List<String> tags;
}
