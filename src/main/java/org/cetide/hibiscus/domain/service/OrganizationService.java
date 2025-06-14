package org.cetide.hibiscus.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cetide.hibiscus.domain.model.aggregate.Organization;
import org.cetide.hibiscus.infrastructure.persistence.entity.OrganizationEntity;
import org.cetide.hibiscus.interfaces.rest.dto.UpdateOrganizationRequest;

import java.util.List;

/**
 * Organization 服务接口
 * @author Hibiscus-code-generate
 */
public interface OrganizationService extends IService<OrganizationEntity> {

    /**
     * 根据 ownerId 获取组织列表
     */
    List<OrganizationEntity> getByOwnerId(Long ownerId);

    /**
     * 更新组织信息
     */
    OrganizationEntity updateOrganizationInfo(Long organizationId, UpdateOrganizationRequest request);
}
