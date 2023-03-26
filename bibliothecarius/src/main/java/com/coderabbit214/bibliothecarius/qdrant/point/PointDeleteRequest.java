package com.coderabbit214.bibliothecarius.qdrant.point;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: Mr_J
 * @Date: 2023/3/22 15:54
 */
@Setter
@Getter
@ToString
public class PointDeleteRequest {
    List<String> points;
}
