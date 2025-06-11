package org.cetide.hibiscus.interfaces.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record PasswordChangeRequest(
        @NotBlank(message = "新密码不能为空")
        @Size(min = 8, message = "密码长度至少8位")
        String newPassword
) {}