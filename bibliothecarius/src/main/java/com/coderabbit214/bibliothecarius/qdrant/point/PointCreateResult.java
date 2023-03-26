package com.coderabbit214.bibliothecarius.qdrant.point;

import com.coderabbit214.bibliothecarius.qdrant.AbstractResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Mr_J
 * @Date: 2023/3/20 09:30
 */
@Setter
@Getter
@ToString
public class PointCreateResult extends AbstractResult {
    private PointResponse result;

    private String status;

}
