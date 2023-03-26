package com.coderabbit214.bibliothecarius.storage;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Mr_J
 */
@Component
public class StorageConstantPropertiesUtils implements InitializingBean {

    @Value("${storage.dir}")
    private String dir;
    @Value("${storage.updateExpiryTime}")
    private Integer updateExpiryTime;
    @Value("${storage.downloadExpiryTime}")
    private Integer downloadExpiryTime;
    @Value("${storage.protocol}")
    private String protocol;

    public static String DIR;
    public static Integer UPDATE_EXPIRY_TIME;
    public static Integer DOWNLOAD_EXPIRY_TIME;
    public static String PROTOCOL;

    @Override
    public void afterPropertiesSet() {
        DIR = dir;
        UPDATE_EXPIRY_TIME = updateExpiryTime;
        DOWNLOAD_EXPIRY_TIME = downloadExpiryTime;
        PROTOCOL = protocol;
    }
}