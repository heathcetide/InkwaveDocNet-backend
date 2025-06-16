package org.cetide.hibiscus.interfaces.task;

import org.cetide.hibiscus.domain.model.aggregate.Document;
import org.cetide.hibiscus.domain.service.DocumentService;
import org.cetide.hibiscus.infrastructure.cache.RedisUtils;
import org.cetide.hibiscus.infrastructure.persistence.mongo.DocumentVersionRepository;
import org.cetide.hibiscus.infrastructure.persistence.mongo.entity.DocumentVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Component
public class SyncDocumengToDataSource {

    private final RedisUtils redisUtils;

    private final DocumentVersionRepository documentVersionRepository;

    private final DocumentService documentService;

    private final static Logger log = LoggerFactory.getLogger(SyncDocumengToDataSource.class);

    public SyncDocumengToDataSource(RedisUtils redisUtils, DocumentVersionRepository documentVersionRepository, DocumentService documentService) {
        this.redisUtils = redisUtils;
        this.documentVersionRepository = documentVersionRepository;
        this.documentService = documentService;
    }

    @Scheduled(fixedDelay = 60000) // 每分钟执行一次
    public void flushPendingDocumentsToStorage() {
        Set<String> docKeys = redisUtils.scanKeys("queue:doc:*");
        for (String queueKey : docKeys) {
            Long documentId = extractDocId(queueKey); // e.g. "queue:doc:123"
            syncFromRedisToStorage(documentId);
        }
    }

    public void syncFromRedisToStorage(Long documentId) {
        String keyPrefix = "doc:" + documentId;

        // 1. 获取 Redis 中的数据
        String content = (String) redisUtils.get(keyPrefix + ":content");
        String title = (String) redisUtils.get(keyPrefix + ":title");
        String versionStr = (String) redisUtils.get(keyPrefix + ":version");
        String editorStr = (String) redisUtils.get(keyPrefix + ":editor");

        if (content == null || title == null || versionStr == null || editorStr == null) {
            log.warn("文档 {} Redis 数据不完整，跳过同步", documentId);
            return;
        }

        int version = Integer.parseInt(versionStr);
        Long editorId = Long.parseLong(editorStr);

        // 2. 写入 MongoDB 快照
        DocumentVersion versionDoc = new DocumentVersion();
        versionDoc.setDocumentId(documentId);
        versionDoc.setContent(content);
        versionDoc.setTitle(title);
        versionDoc.setVersion(version);
        versionDoc.setEditorId(editorId);
        versionDoc.setCreatedAt(Instant.now());

        documentVersionRepository.save(versionDoc);

        // 3. 更新 MySQL 主表 current_version、title 等
        Document doc = documentService.getById(documentId);
        if (doc != null) {
            doc.setVersion(version);
            doc.setCurrentVersionId(versionDoc.getDocumentId()); // Mongo ObjectId -> String
            doc.setTitle(title);
            doc.setUpdatedAt(LocalDateTime.now());
            documentService.updateById(doc);
        }

        log.info("文档 {} 内容已同步至 MongoDB 和 MySQL，版本号为 {}", documentId, version);
    }

    private Long extractDocId(String redisKey) {
        // e.g. "queue:doc:123" → 123L
        String[] parts = redisKey.split(":");
        return Long.parseLong(parts[2]);
    }
}
