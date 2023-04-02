package com.coderabbit214.bibliothecarius.model.external;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Mr_J
 * @since 2023-03-29
 */
@Setter
@Getter
@ToString
public class ModelExternalCheckResult {
    private Boolean success;
    private String message;
}