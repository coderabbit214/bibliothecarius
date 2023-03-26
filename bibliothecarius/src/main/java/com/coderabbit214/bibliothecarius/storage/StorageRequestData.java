package com.coderabbit214.bibliothecarius.storage;

import java.io.Serializable;

/**
 * @Author: Mr_J
 * @Date: 2022/3/11 4:48 下午
 */
public class StorageRequestData implements Serializable {
    /** 请求路径 */
    private String host;
    /** 文件参数key值 */
    private String fileFieldName;
    /** 参数是否使用FormData */
    private boolean withFormData;
    /** 请求方式 */
    private String method;
    /** formData参数 */
    private FormData formData;

    public StorageRequestData(String host, String fileFieldName, boolean withFormData, String method, FormData formData) {
        this.host = host;
        this.fileFieldName = fileFieldName;
        this.withFormData = withFormData;
        this.method = method;
        this.formData = formData;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFileFieldName() {
        return fileFieldName;
    }

    public void setFileFieldName(String fileFieldName) {
        this.fileFieldName = fileFieldName;
    }

    public boolean isWithFormData() {
        return withFormData;
    }

    public void setWithFormData(boolean withFormData) {
        this.withFormData = withFormData;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public FormData getFormData() {
        return formData;
    }

    public void setFormData(FormData formData) {
        this.formData = formData;
    }
}
