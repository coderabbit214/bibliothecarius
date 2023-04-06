package com.coderabbit214.bibliothecarius.storage;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.InputStream;
import java.util.List;

public interface OssService {

    void createBucket(String bucketName);

    List<Bucket> getAllBuckets();

    void removeBucket(String bucketName);

    void putObject(String bucketName, String objectName, InputStream stream, String contextType) throws Exception;

    void putObject(String bucketName, String objectName, InputStream stream) throws Exception;

    S3Object getObject(String bucketName, String objectName);

    String getObjectURL(String bucketName, String objectName, Integer expires);

    void removeObject(String bucketName, String objectName) throws Exception;

    List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive);
}
