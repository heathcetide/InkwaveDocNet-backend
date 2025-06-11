package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.domain.model.aggregate.DocumentVersion;
import org.cetide.hibiscus.domain.service.DocumentVersionService;
import org.cetide.hibiscus.infrastructure.persistence.mapper.DocumentVersionMapper;
import org.springframework.stereotype.Service;

/**
 * DocumentVersion 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class DocumentVersionServiceImpl extends ServiceImpl<DocumentVersionMapper, DocumentVersion> implements DocumentVersionService {

}
