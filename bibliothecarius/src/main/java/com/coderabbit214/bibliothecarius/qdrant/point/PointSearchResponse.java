package com.coderabbit214.bibliothecarius.qdrant.point;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Mr_J
 * @Date: 2023/3/20 09:40
 */
@Setter
@Getter
@ToString
public class PointSearchResponse {
    private String id;
    private long version;
    private float score;
    private Object payload;
    private float[] vector;
}
