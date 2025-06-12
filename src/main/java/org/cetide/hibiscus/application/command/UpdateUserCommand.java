package org.cetide.hibiscus.application.command;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 修改用户命令
 */
public class UpdateUserCommand {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    @Size(max = 100, message = "用户名长度不能超过100字符")
    private String username;

    @Email(message = "邮箱格式错误")
    @Size(max = 100)
    private String email;

    @Size(max = 255)
    private String avatarUrl;

    @Size(max = 50)
    private String status; // ACTIVE / BANNED / DELETED

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}