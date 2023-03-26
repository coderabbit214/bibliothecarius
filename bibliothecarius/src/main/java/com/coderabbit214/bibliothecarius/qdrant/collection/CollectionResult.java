package com.coderabbit214.bibliothecarius.qdrant.collection;

import com.coderabbit214.bibliothecarius.qdrant.AbstractResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Mr_J
 */
@Setter
@Getter
@ToString
public class CollectionResult extends AbstractResult {

    private Boolean result;

    private String status;

}