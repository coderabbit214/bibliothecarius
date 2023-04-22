package com.coderabbit214.bibliothecarius.document;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.coderabbit214.bibliothecarius.common.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
public class DocumentVO implements Serializable {

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

    private List<String> tags;

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

    public void conver(Document document) {
        this.id = document.getId();
        this.datasetId = document.getDatasetId();
        this.name = document.getName();
        this.hashCode = document.getHashCode();
        this.state = document.getState();
        this.tags = JsonUtil.toArray(document.getTags(), String.class);
        this.fileKey = document.getFileKey();
        this.size = document.getSize();
        this.type = document.getType();
        this.createTime = document.getCreateTime();
        this.updateTime = document.getUpdateTime();
    }
}
