package com.coderabbit214.bibliothecarius.qdrant;

public class QdrantHttpException extends RuntimeException {

    /**
     * HTTP status code
     */
    public final int statusCode;


    public QdrantHttpException(QdrantError error, Exception parent, int statusCode) {
        super(error.getStatus().getError(), parent);
        this.statusCode = statusCode;
    }
}
