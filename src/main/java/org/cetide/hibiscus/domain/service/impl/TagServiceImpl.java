package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.domain.model.aggregate.Tag;
import org.cetide.hibiscus.domain.service.TagService;
import org.cetide.hibiscus.infrastructure.persistence.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
 * Tag 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
