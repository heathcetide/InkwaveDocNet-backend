package org.cetide.hibiscus.interfaces.rest.controller;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cetide.hibiscus.application.service.DocumentContentService;
import org.cetide.hibiscus.common.context.AuthContext;
import org.cetide.hibiscus.domain.model.aggregate.Document;
import org.cetide.hibiscus.common.request.PageRequest;
import org.cetide.hibiscus.common.response.ApiResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cetide.hibiscus.domain.service.DocumentService;
import org.cetide.hibiscus.infrastructure.persistence.mongo.entity.DocumentVersion;
import org.cetide.hibiscus.interfaces.rest.dto.CreateDocumentRequest;
import org.cetide.hibiscus.interfaces.rest.dto.DocumentContentUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Document 控制器，提供基础增删改查接口
 *
 * @author Hibiscus-code-generate
 */
@Api(tags = "Document 控制器")
@RestController
@RequestMapping("/api/document")
public class DocumentController {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    /**
     * DocumentService
     */
    private final DocumentService documentService;

    /**
     * DocumentContentService
     */
    private final DocumentContentService documentContentService;


    public DocumentController(DocumentService documentService, DocumentContentService documentContentService) {
        this.documentService = documentService;
        this.documentContentService = documentContentService;
    }

    /**
     * 分页查询文档列表
     */
    @ApiOperation("分页查询文档列表")
    @GetMapping("/list")
    public ApiResponse<Page<Document>> listDocuments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long knowledgeBaseId,
            @RequestParam(required = false) Long parentId
    ) {
        QueryWrapper<Document> query = new QueryWrapper<>();
        if (knowledgeBaseId != null) {
            query.eq("knowledge_base_id", knowledgeBaseId);
        }
        if (parentId != null) {
            query.eq("parent_id", parentId);
        }
        query.eq("deleted", 0);
        query.orderByAsc("`order`");

        Page<Document> pageResult = documentService.page(new Page<>(page, size), query);
        return ApiResponse.success(pageResult);
    }

    /**
     * 新建文档
     */
    @ApiOperation("新建文档")
    @PostMapping("/create")
    public ApiResponse<CreateDocumentRequest> createDocument(@RequestBody CreateDocumentRequest request) {
        Long currentUserId = getCurrentUserId();
        Document document = new Document();
        document.setTitle(request.getTitle());
        document.setOwnerId(currentUserId);
        document.setKnowledgeBaseId(request.getKnowledgeBaseId());
        document.setParentId(request.getParentId());
        document.setLevel(request.getLevel());
        document.setSortOrder(request.getOrder());
        document.setStatus("ACTIVE");
        document.setPublished(true);
        document.setCurrentVersionId(0L);
        document.setMetadata("{}");
        boolean saved = documentService.save(document);
        if (!saved) {
            return ApiResponse.error("创建文档失败");
        }
        request.setId(document.getId());
        return ApiResponse.success(request);
    }

    /**
     * 更新文档
     */
    @ApiOperation("更新文档")
    @PostMapping("/update")
    public ApiResponse<Document> updateDocument(@RequestBody Document document) {
        if (document.getId() == null) {
            return ApiResponse.error("文档ID不能为空");
        }
        boolean updated = documentService.updateById(document);
        if (!updated) {
            return ApiResponse.error("更新文档失败");
        }
        return ApiResponse.success(document);
    }

    /**
     * 删除文档（逻辑删除）
     */
    @ApiOperation("删除文档（逻辑删除）")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteDocument(@PathVariable Long id) {
        Document doc = documentService.getById(id);
        if (doc == null || doc.getDeleted()) {
            return ApiResponse.error("文档不存在");
        }
        doc.setDeleted(true);
        boolean updated = documentService.updateById(doc);
        if (!updated) {
            return ApiResponse.error("删除文档失败");
        }
        return ApiResponse.success();
    }

    /**
     * 根据知识库ID查询当前用户所有文档
     */
    @GetMapping("/listByKnowledgeBase")
    public ApiResponse<List<Document>> listDocumentsByKnowledgeBase(
            @RequestParam Long knowledgeBaseId) {

        Long currentUserId = getCurrentUserId();
        return ApiResponse.success(documentService.listDucumentByKnowledgeBase(knowledgeBaseId, currentUserId));
    }


    /**
     * 保存文档内容
     */
    @ApiOperation("保存文档内容和新版本")
    @PostMapping("/{id}/content")
    public ApiResponse<Void> saveContent(
            @PathVariable Long id,
            @RequestBody DocumentContentUpdateRequest request) {
        if (request.getContent() == null || request.getTitle() == null || request.getContent().isEmpty() || request.getContent().isBlank()) {
            return ApiResponse.success();
        }
        documentContentService.saveNewVersion(id, request.getContent(), request.getTitle(), AuthContext.getCurrentUser().getId(), request.getLatestOp());
        return ApiResponse.success();
    }

    /**
     * 获取最新文档内容
     */
    @ApiOperation("获取最新文档内容")
    @GetMapping("/{id}/content")
    public ApiResponse<DocumentVersion> getLatestContent(@PathVariable Long id) {
        DocumentVersion latest = documentContentService.getLatestContent(id);
        if (latest == null) {
            return ApiResponse.error("文档不存在");
        }
        latest.setId(String.valueOf(id));
        return ApiResponse.success(latest);
    }

    /**
     * 获取文档版本列表
     */
    @ApiOperation("获取文档版本列表")
    @GetMapping("/{id}/versions")
    public ApiResponse<List<DocumentVersion>> getVersionList(@PathVariable Long id) {
        List<DocumentVersion> versions = documentContentService.getVersions(id);
        return ApiResponse.success(versions);
    }

    /**
     * 获取指定版本内容
     */
    @ApiOperation("获取指定版本内容")
    @GetMapping("/{id}/versions/{versionId}")
    public ApiResponse<DocumentVersion> getVersionById(@PathVariable String versionId) {
        DocumentVersion version = documentContentService.getVersionById(versionId);
        return ApiResponse.success(version);
    }

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        return AuthContext.getCurrentUser().getId();
    }
}
