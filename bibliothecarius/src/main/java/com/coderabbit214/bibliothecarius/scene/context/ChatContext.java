package com.coderabbit214.bibliothecarius.scene.context;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 上下文
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-22
 */
@TableName("chat_context")
@Setter
@Getter
@ToString
public class ChatContext implements Serializable {

    private static final long serialVersionUID = 1L;


    private String id;

    /**
     * 序号 从1开始
     */
    private Integer sort;

    /**
     * 用户输入
     */
    private String user;

    /**
     * ai输出
     */
    private String assistant;
}
