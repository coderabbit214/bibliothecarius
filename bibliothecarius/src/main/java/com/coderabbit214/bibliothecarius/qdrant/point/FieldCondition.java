package com.coderabbit214.bibliothecarius.qdrant.point;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FieldCondition {

	private String key;

	private MatchValue match;

	private Range range;

}
