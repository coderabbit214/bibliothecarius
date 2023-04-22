package com.coderabbit214.bibliothecarius.document.aliparser;

import com.aliyun.docmind_api20220711.Client;
import com.aliyun.docmind_api20220711.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.coderabbit214.bibliothecarius.common.utils.JsonUtil;
import com.coderabbit214.bibliothecarius.document.Document;
import com.coderabbit214.bibliothecarius.document.DocumentService;
import com.coderabbit214.bibliothecarius.document.DocumentStateEnum;
import com.coderabbit214.bibliothecarius.document.aliparser.entity.Cell;
import com.coderabbit214.bibliothecarius.document.aliparser.entity.Layout;
import com.coderabbit214.bibliothecarius.document.aliparser.entity.ParserData;
import com.coderabbit214.bibliothecarius.document.qdrant.DocumentQdrant;
import com.coderabbit214.bibliothecarius.document.qdrant.DocumentQdrantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
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


    public ParserData parse(String fileName, String fileUrl) throws Exception {
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
            throw new Exception("submit doc structure job error");
        }
        SubmitDocStructureJobResponseBody body = response.getBody();
        SubmitDocStructureJobResponseBody.SubmitDocStructureJobResponseBodyData data = body.getData();
        String id = data.getId();
        if (Objects.isNull(id)) {
            throw new Exception("submit doc structure job error");
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
                throw new Exception("submit doc structure job error");
            } else {
                Object resultBodyData = docStructureResultBody.getData();
                String result = JsonUtil.toJsonString(resultBodyData);
                ParserData parserData = JsonUtil.toObject(result, ParserData.class);
                return parserData;
            }
        }


    }


}
