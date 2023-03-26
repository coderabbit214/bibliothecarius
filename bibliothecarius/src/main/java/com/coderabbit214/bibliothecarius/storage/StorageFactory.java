package com.coderabbit214.bibliothecarius.storage;

import com.coderabbit214.bibliothecarius.storage.aliyun.AliOssService;
import com.coderabbit214.bibliothecarius.storage.minio.MinIOOssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: Mr_J
 * @Date: 2022/3/10 2:35 下午
 */
@Component
public class StorageFactory {

    @Value("${storage.mode}")
    private String endpoint;

    private final AliOssService aliOssService;

    private final MinIOOssService minIOOssService;

    public StorageFactory(AliOssService aliOssService, MinIOOssService minIOOssService) {
        this.aliOssService = aliOssService;
        this.minIOOssService = minIOOssService;
    }

    public StorageInterface getOssService() {
        if (Constant.ALIYUN.equals(endpoint)) {
            return aliOssService;
        } else if (Constant.MINIO.equals(endpoint)) {
            return minIOOssService;
        } else {
            throw new RuntimeException("Please check the endpoint");
        }
    }
}
