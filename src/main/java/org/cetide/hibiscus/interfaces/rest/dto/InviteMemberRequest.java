package org.cetide.hibiscus.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InviteMemberRequest(
        @JsonProperty("organizationId") Long organizationId,
        @JsonProperty("email") String email,
        @JsonProperty("role") String role
) {}
