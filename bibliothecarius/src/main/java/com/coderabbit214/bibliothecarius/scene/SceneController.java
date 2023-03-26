package com.coderabbit214.bibliothecarius.scene;

import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.coderabbit214.bibliothecarius.common.result.RestResult;
import com.coderabbit214.bibliothecarius.common.result.RestResultUtils;
import com.coderabbit214.bibliothecarius.common.utils.JsonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 情景 前端控制器
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-18
 */
@RestController
@RequestMapping("/scene")
@Tag(name = "scene")
public class SceneController {

    private final SceneService sceneService;

    public SceneController(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get detailed information by id")
    public RestResult<?> getSceneById(@PathVariable("id") Long id) {
        Scene scene = sceneService.getById(id);
        //转化为DTO
        SceneVO sceneVO = new SceneVO();
        BeanUtils.copyProperties(scene, sceneVO);
        sceneVO.setParams(JsonUtil.toObject(scene.getParams(), Object.class));
        return RestResultUtils.success(sceneVO);
    }

    @GetMapping("/list")
    @Operation(summary = "list")
    public RestResult<?> list() {
        List<Scene> sceneList = sceneService.list();
        //转化为DTO
        List<SceneVO> sceneVOList = SceneVO.convert(sceneList);
        return RestResultUtils.success(sceneVOList);
    }

    @PostMapping()
    @Operation(summary = "add")
    public RestResult<?> add(@RequestBody @Validated SceneAddDTO sceneAddDTO) {
        sceneService.add(sceneAddDTO);
        return RestResultUtils.success();
    }

    @PutMapping()
    @Operation(summary = "update")
    public RestResult<?> update(@RequestBody @Validated SceneUpdateDTO sceneUpdateDTO) {
        if (sceneUpdateDTO.getId() == null) {
            throw new BusinessException("id cannot be empty");
        }
        sceneService.update(sceneUpdateDTO);
        return RestResultUtils.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete")
    public RestResult<?> delete(@PathVariable("id") Long id) {
        sceneService.removeById(id);
        return RestResultUtils.success();
    }

    @PostMapping("/{name}/chat")
    @Operation(summary = "chat")
    public RestResult<?> chat(@PathVariable("name") String name, @RequestBody ChatDTO chatDTO) {
        ChatResult chatResult = sceneService.chat(name, chatDTO);
        return RestResultUtils.success(chatResult);
    }
}
