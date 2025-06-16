package org.cetide.hibiscus.interfaces.rest.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cetide.hibiscus.application.service.OrgInviteApplicationService;
import org.cetide.hibiscus.common.config.ServerConfig;
import org.cetide.hibiscus.common.context.AuthContext;
import org.cetide.hibiscus.domain.model.aggregate.OrgInvite;
import org.cetide.hibiscus.domain.service.OrgInviteService;
import org.cetide.hibiscus.common.request.PageRequest;
import org.cetide.hibiscus.common.response.ApiResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cetide.hibiscus.interfaces.rest.dto.InviteCreateDTO;
import org.cetide.hibiscus.interfaces.rest.dto.InviteResponseVO;
import org.cetide.hibiscus.interfaces.rest.dto.OrgInviteVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * OrgInvite 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@Api(tags = "OrgInvite 控制器")
@RestController
@RequestMapping("/api/org_invite")
public class OrgInviteController {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(OrgInviteController.class);

    /**
     * OrgInviteService
     */
    private final OrgInviteApplicationService orgInviteApplicationService;

    /**
     * 服务器配置
     */
    private final ServerConfig serverConfig;

    public OrgInviteController(OrgInviteApplicationService orgInviteApplicationService, ServerConfig serverConfig) {
        this.orgInviteApplicationService = orgInviteApplicationService;
        this.serverConfig = serverConfig;
    }


    /**
     * 创建邀请码
     */
    @ApiOperation("创建邀请码")
    @PostMapping("/create")
    public ApiResponse<InviteResponseVO> createInvite(@RequestBody InviteCreateDTO dto) {
        return ApiResponse.success(orgInviteApplicationService.createInvite(dto));
    }

    /**
     * 跳转到邀请页面
     */
    @GetMapping("/link/{code}")
    @ApiOperation("跳转到邀请页面")
    public ResponseEntity<Void> redirectToInvitePage(@PathVariable String code) {
        OrgInvite invite = orgInviteApplicationService.getByCode(code);
        if (invite == null || invite.isExpired()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // 构造前端页面地址
        URI redirectUri = URI.create(serverConfig.getFontUrl() + "?code=" + code);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 重定向
    }

    /**
     * 验证邀请码
     */
    @GetMapping("/validate/{code}")
    @ApiOperation("验证邀请码")
    public ApiResponse<OrgInviteVO> validateInvite(@PathVariable String code) {
        return ApiResponse.success(orgInviteApplicationService.validateInviteCode(code));
    }

    /**
     * 使用邀请码
     */
    @PostMapping("/use/{code}")
    @ApiOperation("使用邀请码")
    public ApiResponse<Void> useInvite(@PathVariable String code) {
        orgInviteApplicationService.useInvite(code, AuthContext.getCurrentUser().getId());
        return ApiResponse.success();
    }

}
