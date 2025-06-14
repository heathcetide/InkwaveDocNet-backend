package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.domain.model.aggregate.Organization;
import org.cetide.hibiscus.domain.service.OrganizationService;
import org.cetide.hibiscus.infrastructure.persistence.entity.OrganizationEntity;
import org.cetide.hibiscus.infrastructure.persistence.mapper.OrganizationMapper;
import org.cetide.hibiscus.interfaces.rest.dto.UpdateOrganizationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Organization 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, OrganizationEntity> implements OrganizationService {

    public List<OrganizationEntity> getByOwnerId(Long ownerId) {
        return lambdaQuery()
                .eq(OrganizationEntity::getOwnerId, ownerId)
                .eq(OrganizationEntity::getDeleted, false)
                .list();
    }

    @Override
    public OrganizationEntity updateOrganizationInfo(Long organizationId, UpdateOrganizationRequest request) {
        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(organizationId);
        organization.setName(request.getName());
        organization.setDescription(request.getDescription());
        organization.setPublished(request.getPublished());
        organization.setStatus(request.getStatus());
        updateById(organization);
        return getById(organizationId);
    }
}
