package com.coderabbit214.bibliothecarius.document.qdrant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long documentId;
    private String qdrantId;
    private String info;
    private String state;
}
