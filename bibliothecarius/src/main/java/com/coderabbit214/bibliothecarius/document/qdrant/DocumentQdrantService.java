package com.coderabbit214.bibliothecarius.document.qdrant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Mr_J
 * @Date: 2023/3/22 14:46
 */
@Service
public class DocumentQdrantService extends ServiceImpl<DocumentQdrantMapper, DocumentQdrant> {

    /**
     * 根据文档id获取所有的qdrantId
     *
     * @param documentId
     * @return
     */
    public List<String> getQdrantIds(Long documentId) {
        LambdaQueryWrapper<DocumentQdrant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DocumentQdrant::getDocumentId, documentId);
        return this.list(queryWrapper).stream().map(DocumentQdrant::getQdrantId).toList();
    }

    public List<DocumentQdrant> getByDocumentId(Long id) {
        LambdaQueryWrapper<DocumentQdrant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DocumentQdrant::getDocumentId, id);
        return this.list(queryWrapper);
    }

    public void deleteByDocumentId(Long id) {
        this.remove(new LambdaQueryWrapper<DocumentQdrant>().eq(DocumentQdrant::getDocumentId, id));
    }
}
