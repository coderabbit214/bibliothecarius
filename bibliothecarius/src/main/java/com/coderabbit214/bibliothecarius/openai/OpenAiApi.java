package com.coderabbit214.bibliothecarius.openai;

import com.coderabbit214.bibliothecarius.openai.chat.*;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.coderabbit214.bibliothecarius.openai.embedding.*;

public interface OpenAiApi {
    @POST("/v1/chat/completions")
    Single<ChatResult> createChatCompletion(@Body ChatRequest request);

    @POST("/v1/embeddings")
    Single<EmbeddingResult> sentEmbeddings(@Body EmbeddingRequest request);
}