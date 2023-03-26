package com.coderabbit214.bibliothecarius.qdrant.point;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PointResponse {
    @JsonProperty("operation_id")
    private Long operationId;

    private String status;
}