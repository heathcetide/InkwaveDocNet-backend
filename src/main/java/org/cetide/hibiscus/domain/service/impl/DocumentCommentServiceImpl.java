package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.domain.model.aggregate.DocumentComment;
import org.cetide.hibiscus.domain.service.DocumentCommentService;
import org.cetide.hibiscus.infrastructure.persistence.mapper.DocumentCommentMapper;
import org.springframework.stereotype.Service;

/**
 * DocumentComment 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class DocumentCommentServiceImpl extends ServiceImpl<DocumentCommentMapper, DocumentComment> implements DocumentCommentService {

}
