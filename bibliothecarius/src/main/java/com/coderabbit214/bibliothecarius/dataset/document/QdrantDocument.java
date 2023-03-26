package com.coderabbit214.bibliothecarius.dataset.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Mr_J
 * @Date: 2023/3/21 18:12
 */
@Setter
@Getter
@ToString
public class QdrantDocument {
    private Long documentId;
    private String context;
    private String info;
}
