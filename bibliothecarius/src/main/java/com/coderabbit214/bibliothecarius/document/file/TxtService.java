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
public class TxtService implements FileInterface {

    private final DocumentQdrantService documentQdrantService;

    private final DocumentService documentService;


    public TxtService(DocumentService documentService, DocumentQdrantService documentQdrantService) {
        this.documentService = documentService;
        this.documentQdrantService = documentQdrantService;

    }

    @Override
    public List<DocumentQdrant> split(InputStream fileInputStream, Document document) throws Exception {
        try {
            //读取文件内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = reader.readLine();
            List<DocumentQdrant> documentQdrants = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            while (line != null) {
                //根据句子分隔
                if ("".equals(line)) {
                    line = reader.readLine();
                    continue;
                }
                stringBuilder.append(line);
                //去除末尾所有空格
                line = line.trim();
                if (!line.endsWith("。") && !line.endsWith("！") && !line.endsWith("？") && !line.endsWith(".") && !line.endsWith("!") && !line.endsWith("?")) {
                    line = reader.readLine();
                    continue;
                }
                CommonService.addDocumentQdrant(document, documentQdrants, stringBuilder);
                line = reader.readLine();
                stringBuilder = new StringBuilder();
            }
            String recycleParagraph = stringBuilder.toString();
            if (StringUtils.isNotEmpty(recycleParagraph)) {
                CommonService.addDocumentQdrant(document, documentQdrants, stringBuilder);
            }
            documentQdrantService.saveBatch(documentQdrants);
            return documentQdrants;
        } catch (Exception e) {
            log.error("txt file converted to qdrant exception", e);
            document.setState(DocumentStateEnum.ERROR.value());
            documentService.updateById(document);
            throw new Exception("txt file converted to qdrant exception");
        }
    }

}
