package com.coderabbit214.bibliothecarius.document.file;

import com.coderabbit214.bibliothecarius.document.Document;
import com.coderabbit214.bibliothecarius.document.DocumentStateEnum;
import com.coderabbit214.bibliothecarius.document.qdrant.DocumentQdrant;

import java.util.List;
import java.util.UUID;

public class CommonService {
    public static void addDocumentQdrant(Document document, List<DocumentQdrant> documentQdrants, StringBuilder stringBuilder) {
        DocumentQdrant documentQdrant = new DocumentQdrant();
        documentQdrant.setDocumentId(document.getId());
        String id = UUID.randomUUID().toString();
        documentQdrant.setQdrantId(id);
        documentQdrant.setInfo(stringBuilder.toString());
        documentQdrant.setState(DocumentStateEnum.PROCESSING.value());
        documentQdrants.add(documentQdrant);
    }

}
