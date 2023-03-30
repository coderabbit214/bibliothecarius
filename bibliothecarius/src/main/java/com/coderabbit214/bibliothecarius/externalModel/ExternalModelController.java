package com.coderabbit214.bibliothecarius.externalModel;

import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.common.result.RestResult;
import com.coderabbit214.bibliothecarius.common.result.RestResultUtils;
import com.coderabbit214.bibliothecarius.externalModel.ExternalModel;
import com.coderabbit214.bibliothecarius.externalModel.ExternalModelService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 自定义模型表 前端控制器
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-29
 */
@RestController
@RequestMapping("/external/model")
public class ExternalModelController {
    private final ExternalModelService externalModelService;

    public ExternalModelController(ExternalModelService externalModelService) {
        this.externalModelService = externalModelService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "info")
    public RestResult<?> info(@PathVariable Long id) {
        ExternalModel externalModel = externalModelService.getById(id);
        return RestResultUtils.success(externalModel);
    }


    @GetMapping("/list")
    @Operation(summary = "list")
    public RestResult<?> list() {
        List<ExternalModel> externalModelList = externalModelService.list();
        return RestResultUtils.success(externalModelList);
    }

    @PostMapping()
    @Operation(summary = "add")
    public RestResult<?> add(@RequestBody @Validated ExternalModel externalModel) {
        return RestResultUtils.success(externalModelService.add(externalModel));
    }

    @PutMapping()
    @Operation(summary = "update")
    public RestResult<?> update(@RequestBody @Validated ExternalModel externalModel) {
        if (externalModel.getId() == null) {
            throw new BusinessException("id不能为空");
        }
        externalModelService.update(externalModel);
        return RestResultUtils.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete")
    public RestResult<?> delete(@PathVariable("id") Long id) {
        externalModelService.delete(id);
        return RestResultUtils.success();
    }

}
