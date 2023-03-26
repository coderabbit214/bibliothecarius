package com.coderabbit214.bibliothecarius.qdrant.point;

import com.coderabbit214.bibliothecarius.qdrant.AbstractResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Mr_J
 * @Date: 2023/3/22 15:54
 */
@Setter
@Getter
@ToString
public class PointDeleteResult extends AbstractResult {
    private String status;

    private PointResponse result;
}
