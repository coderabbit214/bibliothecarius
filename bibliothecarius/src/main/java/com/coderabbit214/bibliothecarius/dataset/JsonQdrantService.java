package com.coderabbit214.bibliothecarius.dataset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author: Mr_J
 * @Date: 2023/3/22 14:48
 */
@Service
public class JsonQdrantService extends ServiceImpl<JsonQdrantMapper, JsonQdrant> {

    /**
     * 根据datasetId删除
     *
     * @param id
     */
    public void deleteByDatasetId(Long id) {
        this.remove(new LambdaQueryWrapper<JsonQdrant>().eq(JsonQdrant::getDatasetId, id));
    }
}
