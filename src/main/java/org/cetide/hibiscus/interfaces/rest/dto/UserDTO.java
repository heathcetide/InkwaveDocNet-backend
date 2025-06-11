package org.cetide.hibiscus.interfaces.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UserDTO(
        @NotBlank String username,
        @Size(min = 8) String password
) {}