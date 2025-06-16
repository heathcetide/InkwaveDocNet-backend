package org.cetide.hibiscus.application.service;


import com.hibiscus.signal.spring.anno.SignalEmitter;
import com.hibiscus.signal.spring.configuration.SignalContextCollector;
import org.cetide.hibiscus.common.anno.Loggable;
import org.cetide.hibiscus.common.config.ServerConfig;
import org.cetide.hibiscus.common.context.AuthContext;
import org.cetide.hibiscus.common.exception.BusinessException;
import org.cetide.hibiscus.domain.model.aggregate.OrgInvite;
import org.cetide.hibiscus.domain.model.aggregate.Organization;
import org.cetide.hibiscus.domain.model.aggregate.OrganizationMember;
import org.cetide.hibiscus.domain.model.enums.LogType;
import org.cetide.hibiscus.domain.service.OrgInviteService;
import org.cetide.hibiscus.domain.service.OrganizationMemberService;
import org.cetide.hibiscus.domain.service.OrganizationService;
import org.cetide.hibiscus.domain.service.UserService;
import org.cetide.hibiscus.infrastructure.persistence.entity.OrganizationEntity;
import org.cetide.hibiscus.infrastructure.utils.InviteCodeUtil;
import org.cetide.hibiscus.infrastructure.utils.SnowflakeIdGenerator;
import org.cetide.hibiscus.interfaces.rest.dto.InviteCreateDTO;
import org.cetide.hibiscus.interfaces.rest.dto.InviteResponseVO;
import org.cetide.hibiscus.interfaces.rest.dto.OrgInviteVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.cetide.hibiscus.common.constants.UserEventConstants.EVENT_INTER_MEDIATE_USER;
import static org.cetide.hibiscus.domain.model.enums.ResponseCodeEnum.*;

@Service
public class OrgInviteApplicationService {

    private final OrgInviteService orgInviteService;

    private final OrganizationService organizationService;

    private final ServerConfig serverConfig;

    private final UserService userService;

    private final OrganizationMemberService organizationMemberService;

    public OrgInviteApplicationService(OrgInviteService orgInviteService, OrganizationService organizationService, ServerConfig serverConfig, UserService userService, OrganizationMemberService organizationMemberService) {
        this.orgInviteService = orgInviteService;
        this.organizationService = organizationService;
        this.serverConfig = serverConfig;
        this.userService = userService;
        this.organizationMemberService = organizationMemberService;
    }

    /**
     * 创建邀请
     * @param dto 邀请信息
     * @return 邀请信息
     */
    @SignalEmitter("invite.create")
    @Loggable(type = LogType.CONFIGURATION, value = "创建邀请码")
    public InviteResponseVO createInvite(InviteCreateDTO dto) {
        SignalContextCollector.collect(EVENT_INTER_MEDIATE_USER, AuthContext.getCurrentUser());
        OrgInvite invite = orgInviteService.createInvite(dto.getOrganizationId(), AuthContext.getCurrentUser().getId(), dto.getRole(), dto.getMaxUses(), dto.getExpiresAt());
        String baseUrl = serverConfig.getInviteBaseUrl();
        String fullUrl = baseUrl + "/" + invite.getInviteCode();
        return new InviteResponseVO(invite.getInviteCode(), fullUrl, fullUrl);
    }

    /**
     * 根据邀请码获取邀请信息
     * @param code 邀请码
     * @return 邀请信息
     */
    @Loggable(type = LogType.CONFIGURATION, value = "获取邀请信息")
    public OrgInvite getByCode(String code) {
        // 解码短码 -> 邀请ID
        Long inviteId = InviteCodeUtil.decode(code);
        OrgInvite invite = orgInviteService.getById(inviteId);

        if (invite == null || invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            return null;
        }

        return invite;
    }

    /**
     * 验证邀请码
     * @param code 邀请码
     * @return 邀请信息
     */
    @Loggable(type = LogType.CONFIGURATION, value = "验证邀请码")
    public OrgInviteVO validateInviteCode(String code) {
        OrgInvite invite = orgInviteService.getOrgIdByCode(code);
        if (invite == null){
            throw new BusinessException(NOT_FOUND.code(), "邀请码不存在");
        }
        // 构造返回信息
        OrgInviteVO vo = new OrgInviteVO();
        vo.setStatus(OrgInviteVO.Status.VALID.getStatus());
        if (invite.getExpiresAt() != null && invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            vo.setStatus(OrgInviteVO.Status.EXPIRED.getStatus());
        }
        if (invite.getMaxUses() != null && invite.getUsedCount() >= invite.getMaxUses()) {
            vo.setStatus(OrgInviteVO.Status.USED_UP.getStatus());
        }
        OrganizationEntity org = organizationService.getById(invite.getOrganizationId());
        if (org == null) {
            vo.setStatus(OrgInviteVO.Status.INVALID.getStatus());
        }
        vo.setInviteCode(invite.getInviteCode());
        vo.setOrganizationId(org.getId());
        vo.setOrganizationName(org.getName());
        vo.setOrganizationDescription(org.getDescription());
        vo.setInviterUsername(userService.getById(invite.getInviterId()).getUsername());
        vo.setRole(invite.getRole());
        vo.setMaxUses(invite.getMaxUses());
        vo.setUsedCount(invite.getUsedCount());
        vo.setExpiresAt(invite.getExpiresAt());
        vo.setValid(true);
        vo.setReasonIfInvalid(null);
        return vo;
    }

    /**
     * 使用邀请码
     * @param code 邀请码
     * @param userId 用户ID
     */
    @SignalEmitter("invite.use")
    @Loggable(type = LogType.CONFIGURATION, value = "使用邀请码")
    public void useInvite(String code, Long userId) {
        // 1. 查询邀请码
        OrgInvite invite = orgInviteService.getOrgIdByCode(code);
        if (invite == null){
            throw new BusinessException(NOT_FOUND.code(), "邀请码不存在");
        }

        // 2. 校验有效性
        if (invite.getExpiresAt() != null && invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(EXPIRED.code(), "邀请码已过期");
        }

        if (invite.getMaxUses() != null && invite.getUsedCount() >= invite.getMaxUses()) {
            throw new BusinessException(USAGE_LIMIT_EXCEEDED.code(), "邀请码已达最大使用次数");
        }

        // 3. 判断用户是否已加入组织
        boolean alreadyMember = organizationMemberService.lambdaQuery()
                .eq(OrganizationMember::getOrganizationId, invite.getOrganizationId())
                .eq(OrganizationMember::getUserId, userId)
                .exists();

        if (alreadyMember) {
            throw new BusinessException(ALREADY_MEMBER.code(), "您已是该组织成员");
        }

        // 4. 插入新成员
        OrganizationMember member = new OrganizationMember();
        member.setId(SnowflakeIdGenerator.nextId());
        member.setOrganizationId(invite.getOrganizationId());
        member.setUserId(userId);
        member.setRole(invite.getRole()); // 从邀请码获取角色
        member.setStatus("ACTIVE");
        member.setJoinedAt(LocalDateTime.now());
        organizationMemberService.save(member);

        // 5. 更新使用次数
        invite.setUsedCount(invite.getUsedCount() + 1);
        orgInviteService.updateById(invite);
        SignalContextCollector.collect(EVENT_INTER_MEDIATE_USER, AuthContext.getCurrentUser());
    }
}
