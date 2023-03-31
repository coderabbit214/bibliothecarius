package com.coderabbit214.bibliothecarius.vector.external;

import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.common.utils.JsonUtil;
import com.coderabbit214.bibliothecarius.externalVector.ExternalVector;
import com.coderabbit214.bibliothecarius.externalVector.ExternalVectorService;
import com.coderabbit214.bibliothecarius.vector.VectorInterface;
import com.coderabbit214.bibliothecarius.vector.VectorResult;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Mr_J
 * @since 2023-03-29
 */
@Service
public class VectorExternalService implements VectorInterface {

    private final ExternalVectorService externalVectorService;

    public VectorExternalService(ExternalVectorService externalVectorService) {
        this.externalVectorService = externalVectorService;
    }

    @Override
    public List<VectorResult> getVector(String text, String vectorType) {
        ExternalVector externalVector = externalVectorService.getByName(vectorType);
        VectorExternalRequest externalRequest = new VectorExternalRequest();
        externalRequest.setInput(text);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JsonUtil.toJsonString(externalRequest);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Double>> responseEntity = restTemplate.exchange(externalVector.getAddress(), HttpMethod.POST, request, new ParameterizedTypeReference<List<Double>>() {
        });
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new BusinessException("Abnormal service");
        }
        VectorResult vectorResult = new VectorResult();
        vectorResult.setText(text);
        vectorResult.setVector(responseEntity.getBody());
        return List.of(vectorResult);
    }
}