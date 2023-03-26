package com.coderabbit214.bibliothecarius.storage;

import java.io.Serializable;

/**
 * @Author: Mr_J
 * @Date: 2022/3/11 4:52 下午
 */
public class FormData implements Serializable {
    private String OSSAccessKeyId;
    private String policy;
    private String signature;
    private String dir;
    private String key;
    private String fileName;
    private String expire;

    public FormData(String OSSAccessKeyId, String policy, String signature, String dir, String key, String fileName, String expire) {
        this.OSSAccessKeyId = OSSAccessKeyId;
        this.policy = policy;
        this.signature = signature;
        this.dir = dir;
        this.key = key;
        this.fileName = fileName;
        this.expire = expire;
    }

    public String getOSSAccessKeyId() {
        return OSSAccessKeyId;
    }

    public void setOSSAccessKeyId(String OSSAccessKeyId) {
        this.OSSAccessKeyId = OSSAccessKeyId;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }
}
