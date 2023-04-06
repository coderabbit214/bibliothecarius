package com.coderabbit214.bibliothecarius.storage;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;

/**
 * @author Mr_J
 */
@Component
public class StorageUtils implements InitializingBean {

    @Value("${storage.dir:}")
    private String dir;

    @Value("${storage.downloadExpiryTime:3600}")
    private Integer downloadExpiryTime;

    @Value("${storage.bucket-name}")
    private String bucketName;

    public static String DIR;
    public static Integer DOWNLOAD_EXPIRY_TIME;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() {
        DIR = dir;
        DOWNLOAD_EXPIRY_TIME = downloadExpiryTime;
        BUCKET_NAME = bucketName;
    }

    public static String getObjectName(String objectName) {
        LocalDateTime now = LocalDateTime.now();
        objectName = DIR + now.format(new DateTimeFormatterBuilder().appendPattern("yyyy/MM/dd/HH/mm/ss").toFormatter()) + "/" + objectName;
        return objectName;
    }
}