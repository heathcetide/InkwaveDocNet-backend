package org.cetide.hibiscus.interfaces.rest.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cetide.hibiscus.common.context.AuthContext;
import org.cetide.hibiscus.common.response.ApiResponse;
import org.cetide.hibiscus.domain.service.OrganizationMemberService;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.interfaces.rest.dto.InviteMemberRequest;
import org.cetide.hibiscus.interfaces.rest.dto.OrganizationMemberVO;
import org.cetide.hibiscus.interfaces.rest.dto.OrganizationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OrganizationMember 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@Api(tags = "OrganizationMember 控制器")
@RestController
@RequestMapping("/api/organization_member")
public class OrganizationMemberController {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(OrganizationMemberController.class);

    /**
     * OrganizationMemberService
     */
    private final OrganizationMemberService organizationMemberService;

    public OrganizationMemberController(OrganizationMemberService organizationMemberService) {
        this.organizationMemberService = organizationMemberService;
    }

    /**
     * 获取当前用户创建的组织列表
     */
    @GetMapping("/my-organizations")
    @ApiOperation("获取当前用户创建的组织列表")
    public ApiResponse<List<OrganizationVO>> getMyOrganizations() {
        Long userId = AuthContext.getCurrentUser().getId();
        List<OrganizationVO> result = organizationMemberService.getOrganizationsByUserId(userId);
        return ApiResponse.success(result);
    }
}
