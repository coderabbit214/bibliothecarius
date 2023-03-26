package com.coderabbit214.bibliothecarius.storage.aliyun;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Mr_J
 */
@Component
public class AliyunConstantPropertiesUtils implements InitializingBean {

    @Value("${storage.aliyun.endpoint:}")
    private String endpoint;
    @Value("${storage.aliyun.keyid:}")
    private String keyId;
    @Value("${storage.aliyun.keysecret:}")
    private String keySecret;
    @Value("${storage.aliyun.bucketname:}")
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