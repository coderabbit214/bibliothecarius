package com.coderabbit214.bibliothecarius.scene;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.coderabbit214.bibliothecarius.common.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class SceneVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 简介
     */
    private String remark;

    /**
     * 消息输入模版
     */
    private String template;

    /**
     * 数据集id
     */
    private Long datasetId;

    /**
     * 使用模型类型
     */
    private String modelType;

    /**
     * 参数
     */
    private Object params;
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

    public static List<SceneVO> convert(List<Scene> sceneList) {
        List<SceneVO> list = new ArrayList<>();
        for (Scene scene : sceneList) {
            SceneVO sceneVO = new SceneVO();
            BeanUtils.copyProperties(scene, sceneVO);
            sceneVO.setParams(JsonUtil.toObject(scene.getParams(), Object.class));
            list.add(sceneVO);
        }
        return list;
    }
}
