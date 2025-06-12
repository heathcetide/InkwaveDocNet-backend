package org.cetide.hibiscus.interfaces.rest.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.cetide.hibiscus.application.command.CreateUserCommand;
import org.cetide.hibiscus.application.command.DeleteUserCommand;
import org.cetide.hibiscus.application.command.UpdateUserCommand;
import org.cetide.hibiscus.application.dto.UserDTO;
import org.cetide.hibiscus.application.service.UserApplicationService;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.common.request.PageRequest;
import org.cetide.hibiscus.common.response.ApiResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cetide.hibiscus.domain.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
