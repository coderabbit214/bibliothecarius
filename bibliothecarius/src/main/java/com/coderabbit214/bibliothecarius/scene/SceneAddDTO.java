package com.coderabbit214.bibliothecarius.scene;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <p>
 * 情景
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-18
 */
@Setter
@Getter
@ToString
public class SceneAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @NotBlank
    @Length(max = 30)
    private String name;

    /**
     * 简介
     */
    @Length(max = 255)
    private String remark;

    /**
     * 消息输入模版
     */
    @NotBlank
    @Length(max = 255)
    private String template;

    /**
     * 数据集id
     */
    private Long datasetId;

    /**
     * 使用模型类型
     */
    @NotBlank
    private String modelType;

    /**
     * 参数
     */
    private Object params;
}
