package com.coderabbit214.bibliothecarius.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Mr_J
 * @Date: 2022/3/10 2:29 下午
 */
public interface StorageInterface {
    /**
     * 文件上传
     * @param file
     * @param objectName
     * @return
     */
    String uploadFile(MultipartFile file, String objectName);

    /**
     * 判断文件是否存在
     * @param objectName
     * @return
     */
    boolean existFile(String objectName);

    /**
     * 获取上传签名
     * @return
     */
    StorageRequestData getPolicy(String dir, String fileName);

    /**
     * 获取查看签名
     */
    String getExpiration(String objectName);

    /**
     * 文件删除
     * @param objectName
     * @return
     */
    void removeObject(String objectName);
}
