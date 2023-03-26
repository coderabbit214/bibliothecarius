package com.coderabbit214.bibliothecarius.dataset.document;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Mr_J
 * @Date: 2023/3/22 14:46
 */
@Setter
@Getter
@ToString
@TableName("document_qdrant")
public class DocumentQdrant {
    private Long documentId;
    private String qdrantId;
}
