package com.coderabbit214.bibliothecarius.externalVector;


import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.common.result.RestResult;
import com.coderabbit214.bibliothecarius.common.result.RestResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author Mr_J
 * @since 2023-03-31
 */
@RestController
@RequestMapping("/external/vector")
@Tag(name = "Custom Vector Computation")
public class ExternalVectorController {

    private final ExternalVectorService externalVectorService;

    public ExternalVectorController(ExternalVectorService externalVectorService) {
        this.externalVectorService = externalVectorService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "info")
    public RestResult<?> info(@PathVariable Long id) {
        ExternalVector externalVector = externalVectorService.getById(id);
        return RestResultUtils.success(externalVector);
    }


    @GetMapping("/list")
    @Operation(summary = "list")
    public RestResult<?> list() {
        List<ExternalVector> externalVectorList = externalVectorService.list();
        return RestResultUtils.success(externalVectorList);
    }

    @PostMapping()
    @Operation(summary = "add")
    public RestResult<?> add(@RequestBody @Validated ExternalVector externalVector) {
        return RestResultUtils.success(externalVectorService.add(externalVector));
    }

    @PutMapping()
    @Operation(summary = "update")
    public RestResult<?> update(@RequestBody @Validated ExternalVector externalVector) {
        if (externalVector.getId() == null) {
            throw new BusinessException("id不能为空");
        }
        externalVectorService.update(externalVector);
        return RestResultUtils.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete")
    public RestResult<?> delete(@PathVariable("id") Long id) {
        externalVectorService.delete(id);
        return RestResultUtils.success();
    }
}
