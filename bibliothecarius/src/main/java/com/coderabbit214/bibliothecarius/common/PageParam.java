package com.coderabbit214.bibliothecarius.common;

import lombok.Data;

/**
 * @author Mr_J
 */
@Data
public class PageParam {
    private Long current = 1L;
    private Long size = 10L;
}