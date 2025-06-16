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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(OrganizationController.class);

    /**
     * 组织服务
     */
    private final OrganizationApplicationService organizationApplicationService;

    /**
     * Redis 工具类
     */
    private final RedisUtils redisUtils;

    public OrganizationController(OrganizationApplicationService organizationApplicationService, RedisUtils redisUtils) {
        this.organizationApplicationService = organizationApplicationService;
        this.redisUtils = redisUtils;
    }

    /**
     * 创建组织
     * @param request 创建组织请求参数
     * @return 创建成功的组织信息
     */
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

    /**
     * 获取组织详情
     * @param id 组织ID
     * @return 组织详情
     */
    @GetMapping("/{id}")
    @ApiOperation("获取组织详情")
    public ApiResponse<OrganizationVO> getOrganization(@PathVariable String id) {
        return ApiResponse.success(organizationApplicationService.getOrganizationById(id));
    }

    /**
     * 切换当前组织
     * @param id 组织ID
     * @return 切换成功
     */
    @PostMapping("/switch/{id}")
    @ApiOperation("切换当前组织")
    public ApiResponse<Void> switchOrganization(@PathVariable String id) {
        UserEntity currentUser = AuthContext.getCurrentUser();
        redisUtils.set("user:current_org:" + currentUser.getId(), id);
        return ApiResponse.success();
    }

    /**
     * 获取当前组织信息
     * @return 当前组织信息
     */
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

    /**
     * 获取当前用户创建的组织列表
     * @return 组织列表
     */
    @GetMapping("/my")
    @ApiOperation("获取当前用户创建的组织列表")
    public ApiResponse<List<OrganizationVO>> getMyOrganizations() {
        UserEntity currentUser = AuthContext.getCurrentUser();
        List<OrganizationVO> list = organizationApplicationService.getOrganizationsByOwnerId(currentUser.getId());
        return ApiResponse.success(list);
    }

    /**
     * 获取组织列表
     * @return 组织列表
     */
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

    /**
     * 更新组织信息
     * @param id 组织ID
     * @param request 更新组织信息请求参数
     * @return 更新后的组织信息
     */
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
