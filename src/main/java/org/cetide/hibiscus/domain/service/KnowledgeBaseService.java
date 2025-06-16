package org.cetide.hibiscus.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cetide.hibiscus.domain.model.aggregate.KnowledgeBase;

import java.util.List;

/**
 * KnowledgeBase 服务接口
 * @author Hibiscus-code-generate
 */
public interface KnowledgeBaseService extends IService<KnowledgeBase> {

    /**
     * 保存知识库
     */
    KnowledgeBase saveKnowledgeBase(KnowledgeBase knowledgeBase);

    /**
     * 根据用户ID获取知识库
     */
    List<KnowledgeBase> getKnowledgeBaseByOwnerId(Long currentUserId);

    /**
     * 更新知识库
     */
    KnowledgeBase updateKnowledgeBase(KnowledgeBase knowledgeBase);
}

