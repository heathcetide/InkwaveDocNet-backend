package org.cetide.hibiscus.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cetide.hibiscus.domain.model.aggregate.OrganizationMember;
import org.cetide.hibiscus.interfaces.rest.dto.InviteMemberRequest;
import org.cetide.hibiscus.interfaces.rest.dto.OrganizationMemberVO;

import java.util.List;

/**
 * OrganizationMember 服务接口
 * @author Hibiscus-code-generate
 */
public interface OrganizationMemberService extends IService<OrganizationMember> {

    /**
     * 邀请用户加入组织
     * @param request 邀请请求
     * @param inviterId 邀请人ID
     */
    void inviteMember(InviteMemberRequest request, Long inviterId);

    /**
     * 接受邀请
     * @param inviteCode 邀请码
     * @param userId 用户ID
     */
    void acceptInvite(String inviteCode, Long userId);

    /**
     * 移除组织成员
     * @param memberId 成员ID
     */
    void removeMember(Long memberId);

    /**
     * 修改组织成员角色
     * @param memberId 成员ID
     * @param role 角色
     */
    void updateRole(Long memberId, String role);

    /**
     * 获取组织成员列表
     * @param orgId 组织ID
     * @return 组织成员列表
     */
    List<OrganizationMemberVO> getMembersByOrgId(Long orgId);

    /**
     * 获取当前用户在组织中的角色
     * @param orgId 组织ID
     * @return 组织成员信息
     */
    OrganizationMemberVO getByUserAndOrg(Long userId, Long orgId);
}
