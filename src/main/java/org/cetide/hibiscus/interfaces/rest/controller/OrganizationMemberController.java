package org.cetide.hibiscus.interfaces.rest.controller;

import io.swagger.annotations.ApiOperation;
import org.cetide.hibiscus.common.context.AuthContext;
import org.cetide.hibiscus.common.response.ApiResponse;
import org.cetide.hibiscus.domain.service.OrganizationMemberService;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.interfaces.rest.dto.InviteMemberRequest;
import org.cetide.hibiscus.interfaces.rest.dto.OrganizationMemberVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OrganizationMember 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@RestController
@RequestMapping("/api/organization_member")
public class OrganizationMemberController {

    private final OrganizationMemberService organizationMemberService;

    public OrganizationMemberController(OrganizationMemberService organizationMemberService) {
        this.organizationMemberService = organizationMemberService;
    }

    @PostMapping("/invite")
    @ApiOperation("邀请用户加入组织")
    public ApiResponse<Void> inviteMember(@RequestBody InviteMemberRequest request) {
        UserEntity currentUser = AuthContext.getCurrentUser();
        organizationMemberService.inviteMember(request, currentUser.getId());
        return ApiResponse.success();
    }

    @PostMapping("/accept")
    @ApiOperation("接受邀请")
    public ApiResponse<Void> acceptInvite(@RequestParam String inviteCode) {
        UserEntity currentUser = AuthContext.getCurrentUser();
        organizationMemberService.acceptInvite(inviteCode, currentUser.getId());
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("移除组织成员")
    public ApiResponse<Void> removeMember(@PathVariable Long id) {
        organizationMemberService.removeMember(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/role")
    @ApiOperation("修改组织成员角色")
    public ApiResponse<Void> updateRole(@PathVariable Long id, @RequestParam String role) {
        organizationMemberService.updateRole(id, role);
        return ApiResponse.success();
    }

    @GetMapping("/list/{orgId}")
    @ApiOperation("获取组织成员列表")
    public ApiResponse<List<OrganizationMemberVO>> listMembers(@PathVariable Long orgId) {
        return ApiResponse.success(organizationMemberService.getMembersByOrgId(orgId));
    }

    @GetMapping("/me/{orgId}")
    @ApiOperation("获取当前用户在组织中的角色")
    public ApiResponse<OrganizationMemberVO> getCurrentUserInOrg(@PathVariable Long orgId) {
        UserEntity currentUser = AuthContext.getCurrentUser();
        return ApiResponse.success(organizationMemberService.getByUserAndOrg(currentUser.getId(), orgId));
    }
}
