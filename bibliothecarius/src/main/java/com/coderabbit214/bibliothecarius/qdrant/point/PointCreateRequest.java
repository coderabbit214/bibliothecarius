package com.coderabbit214.bibliothecarius.qdrant.point;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: Mr_J
 * @Date: 2023/3/20 09:30
 */
@Setter
@Getter
@ToString
public class PointCreateRequest {
    List<Point> points;
}
