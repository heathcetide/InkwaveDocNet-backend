package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.domain.model.aggregate.KnowledgeBase;
import org.cetide.hibiscus.domain.service.KnowledgeBaseService;
import org.cetide.hibiscus.infrastructure.persistence.mapper.KnowledgeBaseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * KnowledgeBase 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase> implements KnowledgeBaseService {

    /**
     * 保存知识库
     */
    @Override
    public KnowledgeBase saveKnowledgeBase(KnowledgeBase knowledgeBase) {
        this.save(knowledgeBase);
        return knowledgeBase;
    }

    /**
     * 获取当前用户的个人知识库
     */
    @Override
    public List<KnowledgeBase> getKnowledgeBaseByOwnerId(Long currentUserId) {
        return this.list(new QueryWrapper<KnowledgeBase>().eq("owner_id", currentUserId));
    }

    /**
     * 更新知识库信息
     */
    @Override
    public KnowledgeBase updateKnowledgeBase(KnowledgeBase knowledgeBase) {
        return this.updateById(knowledgeBase) ? knowledgeBase : null;
    }
}
