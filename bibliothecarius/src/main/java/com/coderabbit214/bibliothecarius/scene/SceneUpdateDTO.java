package com.coderabbit214.bibliothecarius.scene;

import com.coderabbit214.bibliothecarius.common.utils.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
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
public class SceneUpdateDTO implements Serializable {

    private Long id;


    /**
     * 简介
     */
    @Length(max = 255)
    private String remark;

    /**
     * 消息输入模版
     */
    @Length(max = 255)
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


    public static List<SceneUpdateDTO> convert(List<Scene> sceneList) {
        List<SceneUpdateDTO> list = new ArrayList<>();
        for (Scene scene : sceneList) {
            SceneUpdateDTO sceneAddDTO = new SceneUpdateDTO();
            BeanUtils.copyProperties(scene, sceneAddDTO);
            sceneAddDTO.setParams(JsonUtil.toObject(scene.getParams(), Object.class));
            list.add(sceneAddDTO);
        }
        return list;
    }
}
