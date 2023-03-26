package com.coderabbit214.bibliothecarius.qdrant.point;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Mr_J
 * @Date: 2023/3/20 09:36
 */
@Setter
@Getter
@ToString
public class PointSearchParams {

    @JsonProperty("hnsw_ef")
    private Integer hnswEf;

    private Boolean exact;
}
