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
    @Operation(summary = "When uploading json data, the context field is calculated as an index, other fields are not calculated, and type is a reserved field, which is not allowed.")
    public RestResult<?> uploadJson(@PathVariable("name") String name, @RequestBody JsonNode jsonNode) {
        datasetService.uploadJson(name, jsonNode);
        return RestResultUtils.success();
    }

    @GetMapping("/vector/type")
    @Operation(summary = "Get optional vector calculation method")
    public RestResult<?> getVectorType() {
        List<String> vectorTypes = datasetService.getVectorType();
        return RestResultUtils.success(vectorTypes);
    }

}
