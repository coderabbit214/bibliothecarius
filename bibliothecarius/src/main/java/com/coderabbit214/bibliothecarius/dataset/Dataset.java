package com.coderabbit214.bibliothecarius.dataset;

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
 * 数据集
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-18
 */
@Setter
@Getter
@ToString
public class Dataset implements Serializable {

    public static final String DATA_TYPE_JSON = "json";

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称，唯一
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 解析类型
     */
    private String vectorType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
