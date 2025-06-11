package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.domain.model.aggregate.Organization;
import org.cetide.hibiscus.domain.service.OrganizationService;
import org.cetide.hibiscus.infrastructure.persistence.mapper.OrganizationMapper;
import org.springframework.stereotype.Service;

/**
 * Organization 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

}
