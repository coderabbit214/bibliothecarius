package com.coderabbit214.bibliothecarius.qdrant;

import com.coderabbit214.bibliothecarius.qdrant.collection.CollectionRequest;
import com.coderabbit214.bibliothecarius.qdrant.collection.CollectionResult;
import com.coderabbit214.bibliothecarius.qdrant.point.*;
import io.reactivex.Single;
import retrofit2.http.*;

/**
 * @author Mr_J
 */
public interface QdrantApi {

    /**
     * 创建collection
     * @param name
     * @param request
     * @return
     */
    @PUT("/collections/{collection_name}")
    Single<CollectionResult> createCollection(@Path("collection_name") String name, @Body CollectionRequest request);

    /**
     * 删除collection
     * @param name
     * @return
     */
    @DELETE("/collections/{collection_name}")
    Single<CollectionResult> deleteCollection(@Path("collection_name") String name);

    /**
     * 创建point
     * @param name
     * @param request
     * @return
     */
    @PUT("/collections/{collection_name}/points")
    Single<PointCreateResult> createPoints(@Path("collection_name") String name, @Body PointCreateRequest request);

    /**
     * 查询point
     * @param name
     * @param request
     * @return
     */
    @POST("/collections/{collection_name}/points/search")
    Single<PointSearchResult> searchPoints(@Path("collection_name") String name, @Body PointSearchRequest request);

    /**
     * 删除point
     * @param name
     * @param request
     * @return
     */
    @POST("/collections/{collection_name}/points/delete")
    Single<PointDeleteResult> deletePoints(@Path("collection_name") String name, @Body PointDeleteRequest request);
}