package com.coderabbit214.bibliothecarius.vector.openai;

import com.coderabbit214.bibliothecarius.openai.OpenAiService;
import com.coderabbit214.bibliothecarius.common.utils.TokenUtil;
import com.coderabbit214.bibliothecarius.openai.embedding.Embedding;
import com.coderabbit214.bibliothecarius.openai.embedding.EmbeddingRequest;
import com.coderabbit214.bibliothecarius.openai.embedding.EmbeddingResult;
import com.coderabbit214.bibliothecarius.vector.VectorInterface;
import com.coderabbit214.bibliothecarius.vector.VectorResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Mr_J
 * @Date: 2023/3/20 10:07
 */
@Service
public class OpenAiVectorService implements VectorInterface {

    public static final String TEXT_EMBEDDING_ADA_002 = "text-embedding-ada-002";

    // 官方 8192
    public static final Integer MAX_TOKEN = 4000;

    @Value("${openai.api-key}")
    private String apiKey;

    @Override
    public List<VectorResult> getVector(String text, String vectorType) {
        List<VectorResult> vectorResults = new ArrayList<>();

        OpenAiService openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
        Integer tokens = TokenUtil.getTokens(text);

        List<String> texts = new ArrayList<>();
        if (tokens > MAX_TOKEN) {
            //按句子拆分 。？！!?.
            String[] sentences = text.split("[。？！!?.]");
            for (String sentence : sentences) {
                if (TokenUtil.getTokens(sentence) < MAX_TOKEN) {
                    texts.add(sentence);
                } else {
                    //按照
                    String[] subSentences = sentence.split("[,,]");
                    texts.addAll(Arrays.asList(subSentences));
                }
            }
        } else {
            texts.add(text);
        }

        for (String t : texts) {
            if (t.isEmpty()) {
                continue;
            }
            EmbeddingRequest embeddingRequest = new EmbeddingRequest();
            embeddingRequest.setInput(t);
            embeddingRequest.setModel(TEXT_EMBEDDING_ADA_002);
            EmbeddingResult embeddingResult = openAiService.sentEmbeddings(embeddingRequest);
            List<Embedding> embeddingList = embeddingResult.getData();
            List<Double> embedding = embeddingList.get(0).getEmbedding();
            VectorResult vectorResult = new VectorResult();
            vectorResult.setVector(embedding);
            vectorResult.setText(t);
            vectorResults.add(vectorResult);
        }

        return vectorResults;
    }
}
