package com.coderabbit214.bibliothecarius.qdrant.point;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: Mr_J
 * @Date: 2023/3/20 09:32
 */
@Setter
@Getter
@ToString
public class PointSearchRequest {

    private PointSearchParams params;

    private List<Double> vector;

    private int limit;

    private Filter filter;

    @JsonProperty("with_payload")
    private Boolean withPayload;

    @JsonProperty("with_vector")
    private Boolean withVector;
}
