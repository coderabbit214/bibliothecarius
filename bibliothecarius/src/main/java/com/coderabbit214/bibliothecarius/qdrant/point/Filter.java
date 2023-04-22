package com.coderabbit214.bibliothecarius.qdrant.point;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class Filter {

    /**
     * At least one of those conditions should match.
     */
    private List<FieldCondition> should;

    /**
     * All conditions must match.
     */
    private List<FieldCondition> must;

    /**
     * All conditions must NOT match.
     */
    @JsonProperty("must_not")
    private List<FieldCondition> mustNot;

}
