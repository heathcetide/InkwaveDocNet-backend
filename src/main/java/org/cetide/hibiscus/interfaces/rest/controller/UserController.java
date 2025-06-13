package org.cetide.hibiscus.interfaces.rest.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.cetide.hibiscus.application.command.CreateUserCommand;
import org.cetide.hibiscus.application.command.DeleteUserCommand;
import org.cetide.hibiscus.application.command.SendVerificationCommand;
import org.cetide.hibiscus.application.command.UpdateUserCommand;
import org.cetide.hibiscus.application.dto.UserDTO;
import org.cetide.hibiscus.application.service.UserApplicationService;
import org.cetide.hibiscus.common.request.PageRequest;
import org.cetide.hibiscus.common.response.ApiResponse;
import org.cetide.hibiscus.interfaces.rest.dto.UserRegisterEmailRequest;
import org.cetide.hibiscus.interfaces.rest.dto.UserVO;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import static org.cetide.hibiscus.domain.model.enums.ResponseCodeEnum.SYSTEM_ERROR;

/**
 * User 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserApplicationService userAppService;

    public UserController(UserApplicationService userAppService) {
        this.userAppService = userAppService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
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
     * 邮箱登录
     */
    @PostMapping("/login/email")
    @ApiOperation("邮箱登录账号")
    public ApiResponse<String> loginByEmail(@RequestBody UserRegisterEmailRequest userRegisterEmailRequest, HttpServletRequest request) {
        return ApiResponse.success(userAppService.loginByEmail(userRegisterEmailRequest, request));
    }


    @PostMapping
    public ApiResponse<Void> create(@RequestBody @Valid CreateUserCommand command) {
        userAppService.createUser(command);
        return ApiResponse.success();
    }

    @PutMapping
    public ApiResponse<Void> update(@RequestBody @Valid UpdateUserCommand command) {
        userAppService.updateUser(command);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        DeleteUserCommand command = new DeleteUserCommand();
        command.setId(id);
        userAppService.deleteUser(command);
        return ApiResponse.success();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> get(@PathVariable Long id) {
        return ApiResponse.success(userAppService.getUserById(id));
    }

    @PostMapping("/page")
    public ApiResponse<IPage<UserDTO>> page(@RequestBody PageRequest request) {
        return ApiResponse.success(userAppService.getUserPage(request));
    }
}
