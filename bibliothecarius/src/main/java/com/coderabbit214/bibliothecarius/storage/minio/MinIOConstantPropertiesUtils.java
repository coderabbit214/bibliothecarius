package com.coderabbit214.bibliothecarius.storage.minio;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Mr_J
 */
@Component
public class MinIOConstantPropertiesUtils implements InitializingBean {

    @Value("${storage.minio.endpoint:}")
    private String endpoint;
    @Value("${storage.minio.keyid:}")
    private String keyId;
    @Value("${storage.minio.keysecret:}")
    private String keySecret;
    @Value("${storage.minio.bucketname:}")
    private String bucketName;

    public static String END_POINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET_NAME;
    @Override
    public void afterPropertiesSet() {
        END_POINT = endpoint;
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}