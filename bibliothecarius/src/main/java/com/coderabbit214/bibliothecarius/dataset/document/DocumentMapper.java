package com.coderabbit214.bibliothecarius.dataset.document;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文件数据 Mapper 接口
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-18
 */
@Mapper
public interface DocumentMapper extends BaseMapper<Document> {

    /**
     * 分页查询
     * @param datasetName
     * @param pageParam
     * @param page
     * @return
     */
    IPage<Document> pageByQuery(@Param("datasetName") String datasetName,@Param("pageParam") DocumentQuery pageParam, IPage<Document> page);

    /**
     * 列表
     * @param datasetName
     * @param pageParam
     * @return
     */
    List<Document> listByQuery(@Param("datasetName") String datasetName,@Param("pageParam") DocumentQuery pageParam);
}
