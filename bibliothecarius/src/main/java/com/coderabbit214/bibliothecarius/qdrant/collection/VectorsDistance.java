package com.coderabbit214.bibliothecarius.qdrant.collection;

public enum VectorsDistance {
    COSINE("Cosine"),
    DOT("Dot"),
    EUCLID("Euclid");

    private final String value;

    VectorsDistance(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
