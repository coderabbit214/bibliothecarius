package com.coderabbit214.bibliothecarius.dataset.aliparser;

import com.aliyun.docmind_api20220711.Client;
import com.aliyun.docmind_api20220711.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.coderabbit214.bibliothecarius.common.utils.JsonUtil;
import com.coderabbit214.bibliothecarius.dataset.aliparser.entity.Cell;
import com.coderabbit214.bibliothecarius.dataset.aliparser.entity.Layout;
import com.coderabbit214.bibliothecarius.dataset.aliparser.entity.ParserData;
import com.coderabbit214.bibliothecarius.dataset.document.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AliParserService {

    @Value("${ali-parser.keyid}")
    private String accessKeyId;

    @Value("${ali-parser.keysecret}")
    private String accessKeySecret;

    @Value("${ali-parser.endpoint}")
    private String endpoint;

    private final DocumentService documentService;

    private final DocumentQdrantService documentQdrantService;

    public AliParserService(@Lazy DocumentService documentService, DocumentQdrantService documentQdrantService) {
        this.documentService = documentService;
        this.documentQdrantService = documentQdrantService;
    }


    @Async
    public void parse(Document document, String fileName, String fileUrl, String datasetName, String vectorType) {
        try {
            List<DocumentQdrant> documentQdrants = documentQdrantService.getByDocumentId(document.getId());
            if (documentQdrants.size() > 0) {
                documentService.toVector(documentQdrants, document, vectorType, datasetName);
                return;
            }
            Config config = new Config()
                    .setEndpoint(endpoint)
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessKeySecret);
            Client client = new Client(config);
            SubmitDocStructureJobRequest request = new SubmitDocStructureJobRequest();
            request.fileName = fileName;
            request.fileUrl = fileUrl;
            SubmitDocStructureJobResponse response = client.submitDocStructureJob(request);
            if (response.getStatusCode() != HttpStatus.OK.value()) {
                document.setState(DocumentStateEnum.ERROR.value());
                documentService.updateById(document);
                return;
            }
            SubmitDocStructureJobResponseBody body = response.getBody();
            SubmitDocStructureJobResponseBody.SubmitDocStructureJobResponseBodyData data = body.getData();
            String id = data.getId();
            if (Objects.isNull(id)) {
                document.setState(DocumentStateEnum.ERROR.value());
                documentService.updateById(document);
                return;
            }
            GetDocStructureResultRequest resultRequest = new GetDocStructureResultRequest();
            resultRequest.id = id;

            while (true) {
                //睡眠十秒
                Thread.sleep(10000);
                GetDocStructureResultResponse docStructureResult = client.getDocStructureResult(resultRequest);
                if (docStructureResult.getStatusCode() != HttpStatus.OK.value()) {
                    continue;
                }
                GetDocStructureResultResponseBody docStructureResultBody = docStructureResult.getBody();
                if (!docStructureResultBody.getCompleted()) {
                    continue;
                }
                String status = docStructureResultBody.getStatus();
                if (Objects.equals(status, "Fail")) {
                    document.setState(DocumentStateEnum.ERROR.value());
                    documentService.updateById(document);
                } else {
                    Object resultBodyData = docStructureResultBody.getData();
                    String result = JsonUtil.toJsonString(resultBodyData);
                    ParserData parserData = JsonUtil.toObject(result, ParserData.class);
                    documentQdrants = parserDataToDocumentQdrant(parserData, document);
                    documentService.toVector(documentQdrants, document, vectorType, datasetName);
                }
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            document.setState(DocumentStateEnum.ERROR.value());
            documentService.updateById(document);
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
        documentQdrantService.saveBatch(documentQdrants);
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
