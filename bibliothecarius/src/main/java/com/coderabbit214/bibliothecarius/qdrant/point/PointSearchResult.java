package com.coderabbit214.bibliothecarius.qdrant.point;

import com.coderabbit214.bibliothecarius.qdrant.AbstractResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: Mr_J
 * @Date: 2023/3/20 09:53
 */
@Setter
@Getter
@ToString
public class PointSearchResult extends AbstractResult {
    private List<PointSearchResponse> result;

    private String status;

}
