package org.cetide.hibiscus.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cetide.hibiscus.domain.model.aggregate.Document;
import org.cetide.hibiscus.interfaces.rest.dto.CreateDocumentRequest;

import java.util.List;

/**
 * Document 服务接口
 * @author Hibiscus-code-generate
 */
public interface DocumentService extends IService<Document> {

    List<Document> listDucumentByKnowledgeBase(Long knowledgeBaseId, Long currentUserId);
}
