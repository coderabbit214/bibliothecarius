package com.coderabbit214.bibliothecarius.scene.context;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 上下文
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-22
 */
@Service
public class ChatContextService extends ServiceImpl<ChatContextMapper, ChatContext> {

    public List<ChatContext> listById(String contextId) {
        LambdaQueryWrapper<ChatContext> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatContext::getId, contextId);
        queryWrapper.orderByAsc(ChatContext::getSort);
        return list(queryWrapper);
    }
}
