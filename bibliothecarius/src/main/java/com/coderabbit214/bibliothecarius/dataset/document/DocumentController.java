package com.coderabbit214.bibliothecarius.dataset.document;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coderabbit214.bibliothecarius.common.result.RestResult;
import com.coderabbit214.bibliothecarius.common.result.RestResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * Document Controller
 * </p>
 *
 * @author Mr_J
 * @since 2023-03-18
 */
@RestController
@RequestMapping("/dataset")
@Tag(name = "dataset")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/document/{id}")
    @Operation(summary = "file get")
    public RestResult<?> get(@PathVariable("id") Long id) {
        Document document = documentService.getById(id);
        return RestResultUtils.success(document);
    }

    @PostMapping("/{name}/document")
    @Operation(summary = "file upload")
    public RestResult<?> uploadFile(@PathVariable("name") String name, MultipartFile file) {
        documentService.uploadFile(name, file);
        return RestResultUtils.success();
    }

    @PostMapping("/{name}/reprocess/document/{id}")
    @Operation(summary = "file reprocess")
    public RestResult<?> reprocess(@PathVariable("name") String name, @PathVariable("id") Long id) {
        documentService.reprocess(name, id);
        return RestResultUtils.success();
    }

    @GetMapping("/{name}/document/page")
    @Operation(summary = "file list")
    public RestResult<?> page(@PathVariable("name") String name, DocumentQuery pageParam) {
        IPage<Document> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        page = documentService.pageByQuery(name, pageParam, page);
        return RestResultUtils.success(page);
    }

    @GetMapping("/{name}/document/list")
    @Operation(summary = "file list")
    public RestResult<?> list(@PathVariable("name") String name, DocumentQuery pageParam) {
        List<Document> documents = documentService.listByQuery(name, pageParam);
        return RestResultUtils.success(documents);
    }

    @DeleteMapping("/{name}/document/{id}")
    @Operation(summary = "file delete")
    public RestResult<?> delete(@PathVariable("id") String id, @PathVariable String name) {
        documentService.delete(id, name);
        return RestResultUtils.success();
    }

}
