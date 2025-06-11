package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.domain.model.aggregate.Document;
import org.cetide.hibiscus.domain.service.DocumentService;
import org.cetide.hibiscus.infrastructure.persistence.mapper.DocumentMapper;
import org.springframework.stereotype.Service;

/**
 * Document 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {

}
