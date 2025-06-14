package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.domain.model.aggregate.OrganizationMember;
import org.cetide.hibiscus.infrastructure.cache.RedisUtils;
import org.cetide.hibiscus.infrastructure.persistence.mapper.OrganizationMemberMapper;
import org.cetide.hibiscus.domain.service.OrganizationMemberService;
import org.cetide.hibiscus.interfaces.rest.dto.InviteMemberRequest;
import org.cetide.hibiscus.interfaces.rest.dto.OrganizationMemberVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private final RedisUtils redisUtils;

    public OrganizationMemberServiceImpl(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public void inviteMember(InviteMemberRequest request, Long inviterId) {
        String inviteCode = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        String redisKey = "invite:" + inviteCode;

        Map<String, Object> payload = Map.of(
                "organizationId", request.organizationId(),
                "role", request.role() != null ? request.role() : "MEMBER",
                "email", request.email()
        );

        redisUtils.set(redisKey, payload, 10, TimeUnit.MINUTES); // 邀请有效期10分钟
    }

    @Override
    public void acceptInvite(String inviteCode, Long userId) {
        String redisKey = "invite:" + inviteCode;
        Map<String, Object> payload = redisUtils.get(redisKey, Map.class);

        if (payload == null) {
            throw new IllegalArgumentException("邀请码无效或已过期");
        }

        Long orgId = Long.valueOf(payload.get("organizationId").toString());
        String role = payload.get("role").toString();

        OrganizationMember member = new OrganizationMember();
        member.setOrganizationId(orgId);
        member.setUserId(userId);
        member.setRole(role);
        member.setStatus("ACTIVE");
        member.setJoinedAt(LocalDateTime.now());
        member.setCreatedAt(LocalDateTime.now());
        member.setUpdatedAt(LocalDateTime.now());
        member.setDeleted(false);
        save(member);
        redisUtils.delete(redisKey);
    }

    @Override
    public void removeMember(Long memberId) {
        removeById(memberId);
    }

    @Override
    public void updateRole(Long memberId, String role) {
        OrganizationMember member = getById(memberId);
        if (member != null) {
            member.setRole(role);
            member.setUpdatedAt(LocalDateTime.now());
            updateById(member);
        }
    }

    @Override
    public List<OrganizationMemberVO> getMembersByOrgId(Long orgId) {
        List<OrganizationMember> members = lambdaQuery()
                .eq(OrganizationMember::getOrganizationId, orgId)
                .eq(OrganizationMember::getDeleted, false)
                .list();

        return members.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public OrganizationMemberVO getByUserAndOrg(Long userId, Long orgId) {
        OrganizationMember member = lambdaQuery()
                .eq(OrganizationMember::getOrganizationId, orgId)
                .eq(OrganizationMember::getUserId, userId)
                .eq(OrganizationMember::getDeleted, false)
                .one();

        return member != null ? toVO(member) : null;
    }

    private OrganizationMemberVO toVO(OrganizationMember member) {
        OrganizationMemberVO vo = new OrganizationMemberVO();
        vo.setId(member.getId());
        vo.setUserId(member.getUserId());
        vo.setOrganizationId(member.getOrganizationId());
        vo.setRole(member.getRole());
        vo.setStatus(member.getStatus());
        vo.setJoinedAt(member.getJoinedAt());
        return vo;
    }
}
