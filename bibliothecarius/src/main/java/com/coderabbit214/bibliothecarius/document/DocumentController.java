package com.coderabbit214.bibliothecarius.document;

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
@RequestMapping("/document")
@Tag(name = "document")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "file get")
    public RestResult<?> get(@PathVariable("id") Long id) {
        Document document = documentService.getById(id);
        return RestResultUtils.success(document);
    }

    @PostMapping("/dataset/{name}")
    @Operation(summary = "file upload")
    public RestResult<?> uploadFile(@PathVariable("name") String name, MultipartFile[] files, String[] tags) {
        if (tags == null) {
            tags = new String[]{};
        }
        documentService.uploadFile(name, files, List.of(tags));
        return RestResultUtils.success();
    }

    @PostMapping("/dataset/{name}/reprocess/{id}")
    @Operation(summary = "file reprocess")
    public RestResult<?> reprocess(@PathVariable("name") String name, @PathVariable("id") Long id) {
        documentService.reprocess(name, id);
        return RestResultUtils.success();
    }

    @GetMapping("/dataset/{name}/page")
    @Operation(summary = "file page")
    public RestResult<?> page(@PathVariable("name") String name, DocumentQuery pageParam) {
        IPage<Document> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        page = documentService.pageByQuery(name, pageParam, page);
        return RestResultUtils.success(page);
    }

    @GetMapping("/dataset/{name}/list")
    @Operation(summary = "file list")
    public RestResult<?> list(@PathVariable("name") String name, DocumentQuery pageParam) {
        List<DocumentVO> documents = documentService.listByQuery(name, pageParam);
        return RestResultUtils.success(documents);
    }

    @DeleteMapping("/dataset/{name}/{id}")
    @Operation(summary = "file delete")
    public RestResult<?> delete(@PathVariable("id") String id, @PathVariable String name) {
        documentService.delete(id, name);
        return RestResultUtils.success();
    }

}
