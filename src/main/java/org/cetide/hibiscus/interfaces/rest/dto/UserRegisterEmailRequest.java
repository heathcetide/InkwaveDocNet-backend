package org.cetide.hibiscus.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRegisterEmailRequest(
        @JsonProperty("email") String email,
        @JsonProperty("code") String code
) {}