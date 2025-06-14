package org.cetide.hibiscus.interfaces.rest.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.cetide.hibiscus.application.command.CreateUserCommand;
import org.cetide.hibiscus.application.command.DeleteUserCommand;
import org.cetide.hibiscus.application.command.SendVerificationCommand;
import org.cetide.hibiscus.application.command.UpdateUserCommand;
import org.cetide.hibiscus.application.dto.UserDTO;
import org.cetide.hibiscus.application.service.UserApplicationService;
import org.cetide.hibiscus.common.anno.Loggable;
import org.cetide.hibiscus.common.context.AuthContext;
import org.cetide.hibiscus.common.request.PageRequest;
import org.cetide.hibiscus.common.response.ApiResponse;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.domain.model.enums.LogType;
import org.cetide.hibiscus.infrastructure.persistence.converter.UserConverter;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.interfaces.rest.dto.UpdatePreferencesRequest;
import org.cetide.hibiscus.interfaces.rest.dto.UserRegisterEmailRequest;
import org.cetide.hibiscus.interfaces.rest.dto.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.cetide.hibiscus.domain.model.enums.ResponseCodeEnum.SYSTEM_ERROR;

/**
 * User 控制器，提供基础增删改查接口
 *
 * @author Hibiscus-code-generate
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserApplicationService userAppService;

    public UserController(UserApplicationService userAppService) {
        this.userAppService = userAppService;
    }

    @GetMapping("/send-code/{email}")
    @ApiOperation("发送验证码")
    public ApiResponse<Boolean> sendVerificationCode(@PathVariable String email) {
        SendVerificationCommand command = new SendVerificationCommand(email);
        Boolean isSuccess = userAppService.sendVerificationCode(command);
        if (isSuccess) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error(SYSTEM_ERROR.code(), "发送失败");
        }
    }

    /**
     * 邮箱注册账号
     */
    @PostMapping("/register/email")
    @ApiOperation("邮箱注册账号")
    public ApiResponse<UserVO> registerEmail(@RequestBody UserRegisterEmailRequest userRegisterEmailRequest, HttpServletRequest request) {
        return ApiResponse.success(userAppService.registerByEmail(userRegisterEmailRequest, request));
    }

    /**
     * 根据token获取信息
     */
    @GetMapping("/info")
    @ApiOperation("根据token获取角色信息")
    @Loggable(type = LogType.USER_MANAGEMENT, value = "获取用户信息")
    public ApiResponse<UserVO> getUserInfoByToken() {
        UserEntity currentUser = AuthContext.getCurrentUser();
        if (currentUser != null){
            currentUser.setPassword(null);
            return ApiResponse.success(new UserConverter().toUserVO(currentUser));
        } else {
            return ApiResponse.error(SYSTEM_ERROR.code(), "用户不存在");
        }
    }

    /**
     * 邮箱登录
     */
    @PostMapping("/login/email")
    @ApiOperation("邮箱登录账号")
    public ApiResponse<String> loginByEmail(@RequestBody UserRegisterEmailRequest userRegisterEmailRequest, HttpServletRequest request) {
        return ApiResponse.success(userAppService.loginByEmail(userRegisterEmailRequest, request));
    }

    /**
     * 普通用户修改自己的信息
     */
    @PutMapping("/update")
    @ApiOperation("普通用户修改自己的信息")
    public ApiResponse<String> updateUser(@RequestBody User user) {
        try {
            return ApiResponse.success(userAppService.updateUserInfo(user));
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(SYSTEM_ERROR.code(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating user info", e);
            return ApiResponse.error(SYSTEM_ERROR.code(), "服务器内部错误");
        }
    }

    /**
     * 用户退出登录
     */
    @PostMapping("/logout")
    @ApiOperation("用户退出登录")
    public ApiResponse<String> logout() {
        String currentToken = AuthContext.getCurrentToken();
        userAppService.logoutUser(currentToken);
        return ApiResponse.success("退出登录成功");
    }

    /**
     * 重置密码
     */
    @PostMapping("/reset-password")
    @ApiOperation("重置密码-忘记密码的情况下，通过邮箱或手机号验证身份后设置新密码")
    public ApiResponse<Void> resetPassword(
            @RequestParam String emailOrPhone,
            @RequestParam String newPassword) {
        userAppService.resetPassword(emailOrPhone, newPassword);
        return ApiResponse.success(null);
    }

    /**
     * 上传头像
     */
    @PostMapping("/upload-avatar")
    @ApiOperation("上传头像")
    public ApiResponse<String> uploadAvatar(
            @RequestParam("file") MultipartFile file) {
        String avatarUrl = userAppService.uploadAvatar(file);
        return ApiResponse.success(avatarUrl);
    }

    /**
     * 注销用户
     */
    @PostMapping("/delete-account")
    @ApiOperation("注销用户")
    public ApiResponse<Void> deleteAccount() {
        userAppService.requestAccountDeletion();
        return ApiResponse.success(null);
    }

    /**
     * 更新用户偏好设置
     */
    @PostMapping("/preferences")
    @ApiOperation("更新用户偏好设置（如主题、通知等）")
    public ApiResponse<Void> updatePreferences(@RequestBody UpdatePreferencesRequest request) {
        userAppService.updatePreferences(request);
        return ApiResponse.success();
    }
}
