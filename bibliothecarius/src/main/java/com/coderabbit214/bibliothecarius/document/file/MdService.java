package com.coderabbit214.bibliothecarius.document.file;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.coderabbit214.bibliothecarius.document.Document;
import com.coderabbit214.bibliothecarius.document.DocumentService;
import com.coderabbit214.bibliothecarius.document.DocumentStateEnum;
import com.coderabbit214.bibliothecarius.document.qdrant.DocumentQdrant;
import com.coderabbit214.bibliothecarius.document.qdrant.DocumentQdrantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MdService implements FileInterface {
    private final DocumentQdrantService documentQdrantService;

    private final DocumentService documentService;

    public MdService(DocumentService documentService, DocumentQdrantService documentQdrantService) {
        this.documentService = documentService;
        this.documentQdrantService = documentQdrantService;

    }

    @Override
    public List<DocumentQdrant> split(InputStream fileInputStream, Document document) throws Exception {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            List<DocumentQdrant> documentQdrants = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("## ")) {
                    CommonService.addDocumentQdrant(document, documentQdrants, stringBuilder);
                    stringBuilder = new StringBuilder();
                }
                stringBuilder.append(line).append('\n');
            }
            String recycleParagraph = stringBuilder.toString();
            if (StringUtils.isNotEmpty(recycleParagraph)) {
                CommonService.addDocumentQdrant(document, documentQdrants, stringBuilder);
            }
            documentQdrantService.saveBatch(documentQdrants);
            return documentQdrants;
        } catch (Exception e) {
            log.error("md file converted to qdrant exception", e);
            document.setState(DocumentStateEnum.ERROR.value());
            documentService.updateById(document);
            throw new Exception("md file converted to qdrant exception");
        }
    }

}
