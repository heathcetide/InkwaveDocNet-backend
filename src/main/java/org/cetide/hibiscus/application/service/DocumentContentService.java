package org.cetide.hibiscus.application.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.cetide.hibiscus.domain.model.aggregate.Document;
import org.cetide.hibiscus.domain.service.DocumentService;
import org.cetide.hibiscus.infrastructure.cache.RedisUtils;
import org.cetide.hibiscus.infrastructure.persistence.mongo.DocumentVersionRepository;
import org.cetide.hibiscus.infrastructure.persistence.mongo.entity.DocumentVersion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class DocumentContentService {

    private final DocumentVersionRepository versionRepository;
    private final DocumentService documentService;

    private final RedisUtils redisUtils;

    public DocumentContentService(DocumentVersionRepository versionRepository,
                                  DocumentService documentService, RedisUtils redisUtils) {
        this.versionRepository = versionRepository;
        this.documentService = documentService;
        this.redisUtils = redisUtils;
    }

    public DocumentVersion getLatestVersion(Long documentId) {
        return versionRepository.findTopByDocumentIdOrderByVersionDesc(documentId);
    }

    public List<DocumentVersion> getVersions(Long documentId) {
        return versionRepository.findByDocumentIdOrderByVersionDesc(documentId);
    }

    public DocumentVersion getVersionById(String versionId) {
        return versionRepository.findById(versionId).orElse(null);
    }

    public void saveNewVersion(Long documentId, String content, String title, Long editorId, String operationJson) {
        String keyPrefix = "doc:" + documentId;

        // 缓存当前内容
        redisUtils.set(keyPrefix + ":content", content);
        redisUtils.set(keyPrefix + ":title", title);
        redisUtils.set(keyPrefix + ":editor", String.valueOf(editorId));

        // 初始化版本号为 1（仅第一次）
        redisUtils.setIfAbsent(keyPrefix + ":version", "1");

        // 每次操作都递增版本号
        Long version = redisUtils.increment(keyPrefix + ":version",1);

        // 操作记录追加
        String queueKey = "queue:doc:" + documentId;
        redisUtils.push(queueKey, operationJson);

        // 设置操作队列过期时间
        redisUtils.expire(queueKey, Duration.ofHours(2));
//        Document document = documentService.getById(documentId);
//        if (document == null) throw new IllegalArgumentException("文档不存在");
//
//        Integer newVersion = document.getVersion() + 1;
//
//        DocumentVersion version = new DocumentVersion();
//        version.setDocumentId(documentId);
//        version.setVersion(newVersion);
//        version.setContent(content);
//        version.setEditorId(editorId);
//        version.setCreatedAt(Instant.now());
//        version.setTitle(title);
//
//        DocumentVersion savedVersion = versionRepository.save(version);
//
//        document.setVersion(newVersion);
//        document.setTitle(title);
//        documentService.updateById(document);
//
//        return savedVersion;
    }

    public DocumentVersion getLatestContent(Long docId) {
        String keyPrefix = "doc:" + docId;

        String content = (String) redisUtils.get(keyPrefix + ":content");
        String title = (String) redisUtils.get(keyPrefix + ":title");
        String editorId = (String) redisUtils.get(keyPrefix + ":editor");

        if (StrUtil.isNotBlank(content) && StrUtil.isNotBlank(title)) {
            DocumentVersion version = new DocumentVersion();
            version.setDocumentId(docId);
            version.setContent(content);
            version.setTitle(title);
            version.setEditorId(Long.valueOf(editorId));
            version.setCreatedAt(Instant.now());
            return version;
        }

        // fallback：Redis 没有，再读数据库
        DocumentVersion latest = getLatestVersion(docId);
        if (latest == null) {
            Document doc = documentService.getById(docId);
            if (doc == null || doc.getDeleted()) return null;
            latest = BeanUtil.toBean(doc, DocumentVersion.class);
        }

        return latest;
    }
}