package org.cetide.hibiscus.application.command;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateUserCommand {

    @NotBlank(message = "用户名不能为空")
    @Size(max = 100, message = "用户名长度不能超过100字符")
    private String username;

    @Email(message = "邮箱格式错误")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 255, message = "密码长度应为6-255字符")
    private String password;

    @Size(max = 255)
    private String avatarUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}