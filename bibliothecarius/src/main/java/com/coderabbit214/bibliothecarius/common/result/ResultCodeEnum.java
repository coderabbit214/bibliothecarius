package com.coderabbit214.bibliothecarius.common.result;

/**
 * ResultCodeEnum
 *
 * @author Mr_J
 */
public enum ResultCodeEnum {

    /**
     * Success
     */
    SUCCESS(200, "success"),

    /**
     * Server error
     */
    ERROR(500, "error"),

    /**
     * Operation failed
     */
    FAILED(101, "fail"),

    /**
     * Parameter error
     */
    PARAM_ERROR(103, "parameter error"),

    /**
     * Parameter is null
     */
    INVALID_PARAM_EMPTY(105, "parameter is null"),

    /**
     * Parameter type mismatch
     */
    PARAM_TYPE_MISMATCH(106, "parameter type mismatch"),

    /**
     * Parameter verification failed
     */
    PARAM_VALID_ERROR(107, "parameter verification failed"),

    /**
     * Illegal request
     */
    ILLEGAL_REQUEST(108, "illegal request");

    public int code;

    public String msg;

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
