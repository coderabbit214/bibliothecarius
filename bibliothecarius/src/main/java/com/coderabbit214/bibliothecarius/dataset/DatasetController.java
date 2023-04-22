package com.coderabbit214.bibliothecarius.dataset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.common.result.RestResult;
import com.coderabbit214.bibliothecarius.common.result.RestResultUtils;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 数据集 前端控制器
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-18
 */
@RestController
@RequestMapping("/dataset")
@Tag(name = "dataset")
public class DatasetController {

    private final DatasetService datasetService;

    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "get")
    public RestResult<?> get(@PathVariable("id") Long id) {
        Dataset dataset = datasetService.getById(id);
        return RestResultUtils.success(dataset);
    }


    @GetMapping("/list")
    @Operation(summary = "list")
    public RestResult<?> list() {
        LambdaQueryWrapper<Dataset> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Dataset::getCreateTime);
        List<Dataset> datasetList = datasetService.list(queryWrapper);
        return RestResultUtils.success(datasetList);
    }

    @PostMapping()
    @Operation(summary = "add")
    public RestResult<?> add(@RequestBody @Validated Dataset dataset) {
        return RestResultUtils.success(datasetService.add(dataset));
    }

    @PutMapping()
    @Operation(summary = "update")
    public RestResult<?> update(@RequestBody @Validated Dataset dataset) {
        if (dataset.getId() == null) {
            throw new BusinessException("id不能为空");
        }
        datasetService.update(dataset);
        return RestResultUtils.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete")
    public RestResult<?> delete(@PathVariable("id") Long id) {
        datasetService.delete(id);
        return RestResultUtils.success();
    }

    @PostMapping("/{name}/json")
    @Operation(summary = "JSON data upload, tags use key-value format, used for downstream chat to filter data.")
    public RestResult<?> uploadJson(@PathVariable("name") String name, @RequestBody JsonDTO jsonDTO) {
        datasetService.uploadJson(name, jsonDTO);
        return RestResultUtils.success();
    }

    @GetMapping("/vector/type")
    @Operation(summary = "Get optional vector calculation method")
    public RestResult<?> getVectorType() {
        List<String> vectorTypes = datasetService.getVectorType();
        return RestResultUtils.success(vectorTypes);
    }

    @GetMapping("/{name}/tags")
    @Operation(summary = "Get optional tags")
    public RestResult<?> getTags(@PathVariable("name") String name) {
        Set<String> tags = datasetService.getTags(name);
        return RestResultUtils.success(tags);
    }

}
