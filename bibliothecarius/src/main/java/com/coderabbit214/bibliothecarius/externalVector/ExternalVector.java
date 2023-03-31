package com.coderabbit214.bibliothecarius.externalVector;

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
 *
 * @author Mr_J
 * @since 2023-03-31
 */
@TableName("external_vector")
@Getter
@Setter
@ToString
public class ExternalVector implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotEmpty(message = "name cannot be empty")
    private String name;

    @NotEmpty(message = "size cannot be empty")
    private Integer size;

    private String remark;

    @NotEmpty(message = "address cannot be empty")
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
