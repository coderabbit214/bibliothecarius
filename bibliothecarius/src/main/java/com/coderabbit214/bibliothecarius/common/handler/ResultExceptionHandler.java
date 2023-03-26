package com.coderabbit214.bibliothecarius.common.handler;

import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.common.result.RestResult;
import com.coderabbit214.bibliothecarius.common.result.RestResultUtils;
import com.coderabbit214.bibliothecarius.common.result.ResultCodeEnum;
import jakarta.validation.ConstraintViolationException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Mr_J
 */
@ControllerAdvice
public class ResultExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    /**
     * BusinessException
     */
    @ResponseBody
    @ExceptionHandler(value = {BusinessException.class})
    public RestResult<?> handlerCustomException(BusinessException exception) {
        return RestResultUtils.failedWithMsg(exception.getCode(), exception.getMsg());
    }

    /**
     * MethodArgumentNotValidException
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return RestResultUtils.failedWithMsg(ResultCodeEnum.PARAM_ERROR.getCode(), e.getBindingResult().getFieldError().getField() + e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * ConstraintViolationException
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public RestResult<?> handleMethodArgumentNotValidException(ConstraintViolationException e) {
        return RestResultUtils.failedWithMsg(ResultCodeEnum.PARAM_ERROR.getCode(), e.getMessage().split(": ")[1]);
    }

    /**
     * FileSizeLimitExceededException
     */
    @ResponseBody
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public RestResult<?> handleFileSizeLimitExceededException(FileSizeLimitExceededException e) {
        return RestResultUtils.failedWithMsg(ResultCodeEnum.PARAM_ERROR.getCode(), "maximum support for a single file:" + maxFileSize);
    }

}
