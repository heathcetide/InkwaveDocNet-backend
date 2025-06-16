package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.domain.model.aggregate.Organization;
import org.cetide.hibiscus.domain.model.aggregate.OrganizationMember;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.infrastructure.cache.RedisUtils;
import org.cetide.hibiscus.infrastructure.persistence.entity.OrganizationEntity;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.infrastructure.persistence.mapper.OrganizationMapper;
import org.cetide.hibiscus.infrastructure.persistence.mapper.OrganizationMemberMapper;
import org.cetide.hibiscus.domain.service.OrganizationMemberService;
import org.cetide.hibiscus.infrastructure.persistence.mapper.UserMapper;
import org.cetide.hibiscus.interfaces.rest.dto.InviteMemberRequest;
import org.cetide.hibiscus.interfaces.rest.dto.OrganizationMemberVO;
import org.cetide.hibiscus.interfaces.rest.dto.OrganizationVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * OrganizationMember 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class OrganizationMemberServiceImpl extends ServiceImpl<OrganizationMemberMapper, OrganizationMember> implements OrganizationMemberService {

    private final OrganizationMapper organizationMapper;

    private final UserMapper userMapper;

    public OrganizationMemberServiceImpl(OrganizationMapper organizationMapper, UserMapper userMapper) {
        this.organizationMapper = organizationMapper;
        this.userMapper = userMapper;
    }

    /**
     * 获取用户加入的所有组织
     * @param userId 用户ID
     * @return 组织列表
     */
    @Override
    public List<OrganizationVO> getOrganizationsByUserId(Long userId) {
        // 1. 查询用户加入的所有组织ID
        List<OrganizationMember> members = this.lambdaQuery()
                .eq(OrganizationMember::getUserId, userId)
                .eq(OrganizationMember::getStatus, "ACTIVE")
                .list();

        if (members.isEmpty()) return Collections.emptyList();

        List<Long> orgIds = members.stream()
                .map(OrganizationMember::getOrganizationId)
                .distinct()
                .collect(Collectors.toList());

        // 2. 查询组织信息
        List<OrganizationEntity> organizations = organizationMapper.selectBatchIds(orgIds);

        // 3. 构建 VO 列表
        return organizations.stream().map(org -> {
            OrganizationVO vo = new OrganizationVO();
            vo.setId(org.getId());
            vo.setName(org.getName());
            vo.setDescription(org.getDescription());
            vo.setStatus(org.getStatus());
            vo.setMaxMembers(org.getMaxMembers());
            vo.setPublished(org.getPublished());

            // 4. 查询 owner 用户信息
            UserEntity owner = userMapper.selectById(org.getOwnerId());
            vo.setOwnerUsername(owner.getUsername());
            vo.setOwnerAvatar(owner.getAvatarUrl());

            // 5. 当前成员数
            long count = this.lambdaQuery()
                    .eq(OrganizationMember::getOrganizationId, org.getId())
                    .eq(OrganizationMember::getStatus, "ACTIVE")
                    .count();
            vo.setCurrentMembers(count > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) count);


            return vo;
        }).collect(Collectors.toList());
    }

}
