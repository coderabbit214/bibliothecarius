package com.coderabbit214.bibliothecarius.qdrant;

import com.coderabbit214.bibliothecarius.qdrant.collection.CollectionRequest;
import com.coderabbit214.bibliothecarius.qdrant.point.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.reactivex.Single;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr_J
 */
public class QdrantService {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final ObjectMapper errorMapper = defaultObjectMapper();

    private final QdrantApi api;

    /**
     * Creates a new QdrantService that wraps QdrantApi
     */
    public QdrantService() {
        this(DEFAULT_TIMEOUT);
    }

    /**
     * Creates a new QdrantService that wraps QdrantApi
     *
     * @param timeout http read timeout, Duration.ZERO means no timeout
     */
    public QdrantService(final Duration timeout) {
        this(buildApi(timeout));
    }

    /**
     * Creates a new QdrantService that wraps QdrantApi.
     * Use this if you need more customization.
     *
     * @param api QdrantApi instance to use for all methods
     */
    public QdrantService(final QdrantApi api) {
        this.api = api;
    }

    /**
     * 创建Collection
     *
     * @param name
     * @param request
     */
    public void createCollection(String name, CollectionRequest request) {
        execute(api.createCollection(name, request));
    }

    public void deleteCollection(String name) {
        execute(api.deleteCollection(name));
    }

    public void createPoints(String name, PointCreateRequest request) {
        execute(api.createPoints(name, request));
    }

    public void deletePoints(String name, List<String> points) {
        PointDeleteRequest request = new PointDeleteRequest();
        request.setPoints(points);
        execute(api.deletePoints(name, request));
    }

    public List<PointSearchResponse> searchPoints(String name, PointSearchRequest request) {
        PointSearchResult pointSearchResult = execute(api.searchPoints(name, request));
        return pointSearchResult.getResult();
    }


    /**
     * Calls the Open AI api, returns the response, and parses error messages if the request fails
     */
    public static <T> T execute(Single<T> apiCall) {
        try {
            return apiCall.blockingGet();
        } catch (HttpException e) {
            try {
                if (e.response() == null || e.response().errorBody() == null) {
                    throw e;
                }
                String errorBody = e.response().errorBody().string();

                QdrantError error = errorMapper.readValue(errorBody, QdrantError.class);
                throw new QdrantHttpException(error, e, e.code());
            } catch (IOException ex) {
                throw e;
            }
        }
    }

    public static QdrantApi buildApi(Duration timeout) {
        ObjectMapper mapper = defaultObjectMapper();
        OkHttpClient client = defaultClient(timeout);
        Retrofit retrofit = defaultRetrofit(client, mapper);

        return retrofit.create(QdrantApi.class);
    }

    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper;
    }

    public static OkHttpClient defaultClient(Duration timeout) {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
                .readTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }

    public static Retrofit defaultRetrofit(OkHttpClient client, ObjectMapper mapper) {
        return new Retrofit.Builder()
                .baseUrl(QdrantConfig.HOST)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}