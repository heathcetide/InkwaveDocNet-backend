package org.cetide.hibiscus.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cetide.hibiscus.domain.model.aggregate.OrgInvite;
import org.cetide.hibiscus.interfaces.rest.dto.InviteCreateDTO;
import org.cetide.hibiscus.interfaces.rest.dto.InviteResponseVO;

import java.time.LocalDateTime;

/**
 * OrgInvite 服务接口
 * @author Hibiscus-code-generate
 */
public interface OrgInviteService extends IService<OrgInvite> {

    /**
     * 创建邀请码
     * @param organizationId 组织ID
     * @param inviterId 邀请人ID
     * @param role 角色
     * @param maxUses 最大使用次数
     * @param expiresAt 过期时间
     * @return 邀请码
     */
    OrgInvite createInvite(Long organizationId, Long inviterId, String role, Integer maxUses, LocalDateTime expiresAt);

    /**
     * 根据邀请码获取组织ID
     * @param code 邀请码
     * @return 组织ID
     */
    OrgInvite getOrgIdByCode(String code);
}
