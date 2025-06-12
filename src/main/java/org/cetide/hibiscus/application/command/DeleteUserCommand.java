package org.cetide.hibiscus.application.command;


import javax.validation.constraints.NotNull;

/**
 * 删除用户命令（逻辑删除）
 */
public class DeleteUserCommand {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}