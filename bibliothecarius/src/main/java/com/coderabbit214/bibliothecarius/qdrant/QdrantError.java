package com.coderabbit214.bibliothecarius.qdrant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Mr_J
 */
@Setter
@Getter
@ToString
public class QdrantError extends AbstractResult {

    private QdrantErrorStatus status;

    private Object result;

}