package org.cetide.hibiscus.application.service;

import cn.hutool.core.bean.BeanUtil;
import org.cetide.hibiscus.domain.model.aggregate.Organization;
import org.cetide.hibiscus.domain.service.OrganizationService;
import org.cetide.hibiscus.domain.service.UserService;
import org.cetide.hibiscus.infrastructure.persistence.entity.OrganizationEntity;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.interfaces.rest.dto.CreateOrganizationRequest;
import org.cetide.hibiscus.interfaces.rest.dto.OrganizationVO;
import org.cetide.hibiscus.interfaces.rest.dto.UpdateOrganizationRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationApplicationService {

    private final OrganizationService organizationService;

    private final UserService userService;

    public OrganizationApplicationService(OrganizationService organizationService, UserService userService) {
        this.organizationService = organizationService;
        this.userService = userService;
    }

    public OrganizationVO getOrganizationById(String id) {
        OrganizationEntity byId = organizationService.getById(id);
        UserEntity byId1 = userService.getById(byId.getOwnerId());
        OrganizationVO bean = new OrganizationVO();
        BeanUtil.copyProperties(byId, bean);
        bean.setOwnerUsername(byId1.getUsername());
        bean.setOwnerAvatar(byId1.getAvatarUrl());
        return bean;
    }

    public OrganizationVO createOrganization(CreateOrganizationRequest request, Long id) {
        OrganizationEntity bean = new OrganizationEntity();
        bean.setDescription(request.description());
        bean.setName(request.name());
        bean.setOwnerId(id);
        bean.setPublished(request.published());
        bean.setMaxMembers(request.maxMembers());
        bean.setCurrentMembers(1);
        organizationService.save(bean);
        return convertToVO(bean);
    }

    public List<OrganizationVO> getOrganizationsByOwnerId(Long ownerId) {
        List<OrganizationEntity> organizations = organizationService.getByOwnerId(ownerId);
        return organizations.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    private OrganizationVO convertToVO(OrganizationEntity entity) {
        OrganizationVO vo = new OrganizationVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setDescription(entity.getDescription());
        vo.setStatus(entity.getStatus());
        vo.setMaxMembers(entity.getMaxMembers());
        vo.setCurrentMembers(entity.getCurrentMembers());
        vo.setPublished(entity.getPublished());
        UserEntity owner = userService.getById(entity.getOwnerId());
        if (owner != null) {
            vo.setOwnerUsername(owner.getUsername());
            vo.setOwnerAvatar(owner.getAvatarUrl());
        }
        return vo;
    }

    public void deleteOrganization(Long organizationId) {
        // 你可以在这里检查组织的状态，或者删除与组织相关的数据
        organizationService.removeById(organizationId);
    }

    public OrganizationVO updateOrganization(Long organizationId, UpdateOrganizationRequest request) {
        // 更新组织信息
        OrganizationEntity updatedOrg = organizationService.updateOrganizationInfo(organizationId, request);

        // 转换为 VO 对象返回
        return convertToVO(updatedOrg);
    }
}
