package com.coderabbit214.bibliothecarius.document;

public enum DocumentStateEnum {

    WAIT("wait"),
    PROCESSING("processing"),
    COMPLETE("complete"),
    ERROR("error");

    private final String value;

    DocumentStateEnum(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
