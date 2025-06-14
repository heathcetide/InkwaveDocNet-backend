package org.cetide.hibiscus.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateOrganizationRequest(
        @JsonProperty("name") String name,

        @JsonProperty("description") String description,

        @JsonProperty("max_members") Integer maxMembers,

        @JsonProperty("published") Boolean published
) {
}
