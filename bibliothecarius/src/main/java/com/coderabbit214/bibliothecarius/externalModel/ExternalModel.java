package com.coderabbit214.bibliothecarius.externalModel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * External Model
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-29
 */
@TableName("external_model")
@Setter
@Getter
@ToString
public class ExternalModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotEmpty(message = "name address cannot be empty")
    private String name;

    private String remark;

    private Integer inputMaxToken;

    @NotEmpty(message = "Chat request address cannot be empty")
    private String chatAddress;

    private String checkParametersAddress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
