package com.coderabbit214.bibliothecarius.qdrant.point;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class Payload {
    private String context;
    private String type;
    private Long documentId;
    // Describe relevant information，For example, the nth line.
    private String info;
    private List<String> tags;
}
