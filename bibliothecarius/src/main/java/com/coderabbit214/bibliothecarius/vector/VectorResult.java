package com.coderabbit214.bibliothecarius.vector;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class VectorResult {

    private List<Double> vector;

    private String text;

}
