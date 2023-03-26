package com.coderabbit214.bibliothecarius.common.exception;


import com.coderabbit214.bibliothecarius.common.result.ResultCodeEnum;

/**
 * Exception in business logic
 *
 * @author Mr_J
 */
public class BusinessException extends RuntimeException {

    private int code;

    private String msg;

    public BusinessException() {
        this.code = ResultCodeEnum.FAILED.code;
        this.msg = ResultCodeEnum.FAILED.msg;
    }

    public BusinessException(String message) {
        this.code = ResultCodeEnum.FAILED.code;
        this.msg = message;
    }

    public BusinessException(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.code;
        this.msg = resultCodeEnum.msg;
    }

    public BusinessException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
