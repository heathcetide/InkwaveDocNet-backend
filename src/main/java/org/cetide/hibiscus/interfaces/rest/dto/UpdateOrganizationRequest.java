package org.cetide.hibiscus.interfaces.rest.dto;


public class UpdateOrganizationRequest {
    private String name; // 组织名称
    private String description; // 组织描述
    private String status; // 组织状态（ACTIVE / BANNED / ARCHIVED）
    private Boolean published; // 是否公开（0 - 否，1 - 是）

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }
}
