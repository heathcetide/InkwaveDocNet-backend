package org.cetide.hibiscus.interfaces.rest.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cetide.hibiscus.application.service.OrganizationApplicationService;
import org.cetide.hibiscus.common.context.AuthContext;
import org.cetide.hibiscus.domain.model.aggregate.Organization;
import org.cetide.hibiscus.common.request.PageRequest;
import org.cetide.hibiscus.common.response.ApiResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cetide.hibiscus.domain.service.OrganizationService;
import org.cetide.hibiscus.infrastructure.cache.RedisUtils;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.interfaces.rest.dto.CreateOrganizationRequest;
import org.cetide.hibiscus.interfaces.rest.dto.OrganizationVO;
import org.cetide.hibiscus.interfaces.rest.dto.UpdateOrganizationRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Organization 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@Api(tags = "Organization 控制器")
@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    private final OrganizationApplicationService organizationApplicationService;

    private final RedisUtils redisUtils;

    public OrganizationController(OrganizationApplicationService organizationApplicationService, RedisUtils redisUtils) {
        this.organizationApplicationService = organizationApplicationService;
        this.redisUtils = redisUtils;
    }

    @PostMapping
    @ApiOperation("创建组织")
    public ApiResponse<OrganizationVO> createOrganization(@RequestBody CreateOrganizationRequest request) {
        UserEntity currentUser = AuthContext.getCurrentUser();
        String redisKey = "org:create:" + currentUser.getId();
        if (!redisUtils.setIfAbsent(redisKey, "1", 5, TimeUnit.MINUTES)) {
            return ApiResponse.error("您操作太频繁，请稍后再试");
        }
        return ApiResponse.success(organizationApplicationService.createOrganization(request, currentUser.getId()));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取组织详情")
    public ApiResponse<OrganizationVO> getOrganization(@PathVariable String id) {
        return ApiResponse.success(organizationApplicationService.getOrganizationById(id));
    }

    @PostMapping("/switch/{id}")
    @ApiOperation("切换当前组织")
    public ApiResponse<Void> switchOrganization(@PathVariable String id) {
        UserEntity currentUser = AuthContext.getCurrentUser();
        redisUtils.set("user:current_org:" + currentUser.getId(), id);
        return ApiResponse.success();
    }

    @GetMapping("/current")
    @ApiOperation("获取当前组织信息")
    public ApiResponse<OrganizationVO> getCurrentOrganization() {
        UserEntity currentUser = AuthContext.getCurrentUser();
        String currentOrgId = redisUtils.get("user:current_org:" + currentUser.getId(), String.class);
        if (currentOrgId == null) {
            return ApiResponse.error("当前未选择组织");
        }
        OrganizationVO organization = organizationApplicationService.getOrganizationById(currentOrgId);
        return ApiResponse.success(organization);
    }

    @GetMapping("/my")
    @ApiOperation("获取当前用户创建的组织列表")
    public ApiResponse<List<OrganizationVO>> getMyOrganizations() {
        UserEntity currentUser = AuthContext.getCurrentUser();
        List<OrganizationVO> list = organizationApplicationService.getOrganizationsByOwnerId(currentUser.getId());
        return ApiResponse.success(list);
    }

    @PostMapping("/{id}/delete")
    @ApiOperation("解散团队")
    public ApiResponse<String> deleteOrganization(@PathVariable Long id) {
        try {
            organizationApplicationService.deleteOrganization(id);
            return ApiResponse.success("团队解散成功");
        } catch (Exception e) {
            return ApiResponse.error("解散团队失败", e.getMessage());
        }
    }

    @PostMapping("/{id}/update")
    @ApiOperation("更改团队信息")
    public ApiResponse<OrganizationVO> updateOrganization(@PathVariable Long id, @RequestBody UpdateOrganizationRequest request) {
        try {
            OrganizationVO updatedOrganization = organizationApplicationService.updateOrganization(id, request);
            return ApiResponse.success(updatedOrganization);
        } catch (Exception e) {
            return ApiResponse.error("更改团队信息失败", e.getMessage());
        }
    }
}
