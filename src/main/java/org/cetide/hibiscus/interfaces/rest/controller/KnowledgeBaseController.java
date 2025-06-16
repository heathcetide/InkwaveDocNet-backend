package org.cetide.hibiscus.interfaces.rest.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cetide.hibiscus.common.context.AuthContext;
import org.cetide.hibiscus.common.exception.AuthorizationException;
import org.cetide.hibiscus.common.response.ApiResponse;
import org.cetide.hibiscus.domain.model.aggregate.KnowledgeBase;
import org.cetide.hibiscus.domain.service.KnowledgeBaseService;
import org.cetide.hibiscus.infrastructure.storage.FileStorageAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.cetide.hibiscus.common.constants.KnowledgeBaseConstants.DEFAULT_KNOWLEDGE_BASE_COVER_URL;

/**
 * KnowledgeBase 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@Api(tags = "KnowledgeBase 控制器")
@RestController
@RequestMapping("/api/knowledge_base")
public class KnowledgeBaseController {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseController.class);

    /**
     * KnowledgeBase 服务类
     */
    private final KnowledgeBaseService knowledgeBaseService;

    /**
     * 文件存储适配器
     */
    private final FileStorageAdapter fileStorageAdapter;

    public KnowledgeBaseController(KnowledgeBaseService knowledgeBaseService, FileStorageAdapter fileStorageAdapter) {
        this.knowledgeBaseService = knowledgeBaseService;
        this.fileStorageAdapter = fileStorageAdapter;
    }

    /**
     * 创建个人知识库
     */
    @ApiOperation("当前登录用户创建个人知识库")
    @PostMapping("/createPersonal")
    public ApiResponse<KnowledgeBase> createPersonalKnowledgeBase(@RequestBody KnowledgeBase knowledgeBase) {
        // 1. 获取当前登录用户ID，假设通过安全上下文获取
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new AuthorizationException("当前用户未登录");
        }
        // 2. 设置ownerId和organizationId为null（个人知识库）
        knowledgeBase.setOwnerId(currentUserId);
        knowledgeBase.setOrganizationId(null);
        knowledgeBase.setCoverUrl(knowledgeBase.getCoverUrl() == null ? DEFAULT_KNOWLEDGE_BASE_COVER_URL : knowledgeBase.getCoverUrl());
        // 3. 调用Service保存
        return ApiResponse.success(knowledgeBaseService.saveKnowledgeBase(knowledgeBase));
    }

    /**
     * 获取当前用户的个人知识库
     */
    @ApiOperation("获取当前用户的个人知识库")
    @GetMapping("/getPersonal")
    public ApiResponse<List<KnowledgeBase>> getPersonalKnowledgeBase() {
        // 1. 获取当前登录用户ID，假设通过安全上下文获取
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new AuthorizationException("当前用户未登录");
        }
        // 2. 调用Service获取
        List<KnowledgeBase> knowledgeBase = knowledgeBaseService.getKnowledgeBaseByOwnerId(currentUserId);
        return ApiResponse.success(knowledgeBase);
    }

    /**
     * 更新知识库信息
     */
    @ApiOperation("更新知识库信息")
    @PostMapping("/update")
    public ApiResponse<KnowledgeBase> updateKnowledgeBase(@RequestBody KnowledgeBase knowledgeBase) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new AuthorizationException("当前用户未登录");
        }
        knowledgeBase.setOwnerId(currentUserId);
        return ApiResponse.success(knowledgeBaseService.updateKnowledgeBase(knowledgeBase));
    }

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        return AuthContext.getCurrentUser().getId();
    }
}
