package com.coderabbit214.bibliothecarius.dataset;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Mr_J
 * @Date: 2023/3/22 14:48
 */
@Setter
@Getter
@ToString
@TableName("json_qdrant")
public class JsonQdrant {
    private Long datasetId;
    private String qdrantId;

    private String info;
}
