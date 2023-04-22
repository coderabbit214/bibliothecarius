package com.coderabbit214.bibliothecarius.qdrant.point;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Range {

	@JsonProperty("lt")
	private Double lessThan;

	@JsonProperty("gt")
	private Double greaterThan;

	@JsonProperty("gte")
	private Double greaterThanEqual;

	@JsonProperty("lte")
	private Double lessThanEqual;

}
