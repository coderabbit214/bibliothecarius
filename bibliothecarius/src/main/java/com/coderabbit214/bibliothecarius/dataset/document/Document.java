package com.coderabbit214.bibliothecarius.dataset.document;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * Dataset
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-18
 */
@Setter
@Getter
@ToString
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Dataset id
     */
    private Long datasetId;

    /**
     * name
     */
    private String name;

    /**
     * hashCode
     */
    private String hashCode;

    /**
     * state
     */
    private String state;

    /**
     * file key in S3, MINIO or OSS
     */
    private String fileKey;

    /**
     * size of file
     */
    private Long size;

    /**
     * type of file
     */
    private String type;

    /**
     * create time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * update time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
