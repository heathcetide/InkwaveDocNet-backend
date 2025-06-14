package org.cetide.hibiscus.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdatePreferencesRequest(
        @JsonProperty("themeDark") Boolean themeDark,
        @JsonProperty("emailNotifications") Boolean emailNotifications
) {
    @JsonCreator
    public UpdatePreferencesRequest {}
}
