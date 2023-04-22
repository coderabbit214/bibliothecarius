package com.coderabbit214.bibliothecarius.document.file;

import com.coderabbit214.bibliothecarius.document.*;
import com.coderabbit214.bibliothecarius.document.qdrant.DocumentQdrant;

import java.io.InputStream;
import java.util.List;

/**
 * @author Mr_J
 * @since 2023-04-19
 */
public interface FileInterface {
    String FILE_TYPE_TXT = "txt";

    String FILE_TYPE_MD = "md";

    String FILE_TYPE_PDF = "pdf";

    List<String> SUPPORT_FILE_TYPE = List.of(FILE_TYPE_TXT, FILE_TYPE_MD, FILE_TYPE_PDF);

    /**
     * File splitting
     *
     * @param fileInputStream
     * @param document
     */
    List<DocumentQdrant> split(InputStream fileInputStream, Document document) throws Exception;

}
