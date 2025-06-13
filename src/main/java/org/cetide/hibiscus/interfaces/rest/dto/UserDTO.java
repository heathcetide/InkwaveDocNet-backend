package org.cetide.hibiscus.interfaces.rest.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UserDTO(
        @Email @NotBlank String email,
        @Size(min = 8) String password
) {}