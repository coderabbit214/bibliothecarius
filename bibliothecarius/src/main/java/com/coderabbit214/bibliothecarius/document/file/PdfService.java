package com.coderabbit214.bibliothecarius.document.file;

import com.coderabbit214.bibliothecarius.document.Document;
import com.coderabbit214.bibliothecarius.document.DocumentService;
import com.coderabbit214.bibliothecarius.document.DocumentStateEnum;
import com.coderabbit214.bibliothecarius.document.aliparser.AliParserService;
import com.coderabbit214.bibliothecarius.document.aliparser.entity.Cell;
import com.coderabbit214.bibliothecarius.document.aliparser.entity.Layout;
import com.coderabbit214.bibliothecarius.document.aliparser.entity.ParserData;
import com.coderabbit214.bibliothecarius.document.qdrant.DocumentQdrant;
import com.coderabbit214.bibliothecarius.document.qdrant.DocumentQdrantService;
import com.coderabbit214.bibliothecarius.storage.S3Service;
import com.coderabbit214.bibliothecarius.storage.StorageUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class PdfService implements FileInterface {

    private final S3Service s3Service;

    private final AliParserService aliParserService;

    private final DocumentService documentService;

    private final DocumentQdrantService documentQdrantService;

    public PdfService(S3Service s3Service, AliParserService aliParserService, DocumentService documentService, DocumentQdrantService documentQdrantService) {
        this.s3Service = s3Service;
        this.aliParserService = aliParserService;
        this.documentService = documentService;
        this.documentQdrantService = documentQdrantService;
    }

    @Override
    public List<DocumentQdrant> split(InputStream fileInputStream, Document document) throws Exception {
        try {
            String fileUrl = s3Service.getObjectURL(StorageUtils.BUCKET_NAME, document.getFileKey(), StorageUtils.DOWNLOAD_EXPIRY_TIME);
            ParserData parserData = aliParserService.parse(document.getName(), fileUrl);
            List<DocumentQdrant> documentQdrants = parserDataToDocumentQdrant(parserData, document);
            documentQdrantService.saveBatch(documentQdrants);
            return documentQdrants;
        } catch (Exception e) {
            e.printStackTrace();
            document.setState(DocumentStateEnum.ERROR.value());
            documentService.updateById(document);
            throw new Exception("pdf file converted to qdrant exception");
        }
    }

    /**
     * @param parserData
     * @param document
     */
    public List<DocumentQdrant> parserDataToDocumentQdrant(ParserData parserData, Document document) {
        List<DocumentQdrant> documentQdrants = new ArrayList<>();
        List<Layout> layouts = parserData.getLayouts();
        for (Layout layout : layouts) {
            String text = this.getLayoutText(layout);
            if (text.trim().equals("")) {
                continue;
            }
            DocumentQdrant documentQdrant = new DocumentQdrant();
            documentQdrant.setDocumentId(document.getId());
            documentQdrant.setInfo(text);
            documentQdrant.setState(DocumentStateEnum.PROCESSING.value());
            documentQdrant.setQdrantId(UUID.randomUUID().toString());
            documentQdrants.add(documentQdrant);
        }
        return documentQdrants;
    }


    private String getLayoutText(Layout layout) {
        StringBuilder text = new StringBuilder();
        if (layout.getType().equals("table")) {
            List<Cell> cells = layout.getCells();
            Map<Integer, String> tableText = new LinkedHashMap<>();
            for (Cell cell : cells) {
                List<Layout> layouts = cell.getLayouts();
                StringBuilder sb = new StringBuilder();
                for (Layout la : layouts) {
                    String layoutText = this.getLayoutText(la);
                    sb.append(layoutText);
                    sb.append(" ");
                }
                if (!tableText.containsKey(cell.getXsc())) {
                    tableText.put(cell.getXsc(), sb.toString());
                } else {
                    tableText.put(cell.getXsc(), tableText.get(cell.getXsc()) + " " + sb);
                }
            }
            for (Integer integer : tableText.keySet()) {
                String str = tableText.get(integer);
                text.append(str);
                text.append("\n");
            }
        } else {
            text = new StringBuilder(layout.getText());
        }
        return text.toString();
    }
}
