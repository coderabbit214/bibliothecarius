package com.coderabbit214.bibliothecarius.qdrant.point;

public enum PayloadTypeEnum {

    JSON("json"),
    FILE("file");

    private final String value;

    PayloadTypeEnum(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
