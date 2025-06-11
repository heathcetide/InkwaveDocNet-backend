package org.cetide.hibiscus.domain.model.aggregate;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * KnowledgeBase 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_knowledge_base")
public class KnowledgeBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId
    private Long id;

    /**
    * 知识库名称
    */
    @TableField("name")
    private String name;

    /**
    * 知识库描述
    */
    @TableField("description")
    private String description;

    /**
    * 知识库类型（PRIVATE / PUBLIC）
    */
    @TableField("type")
    private String type;

    /**
    * 所有者用户ID
    */
    @TableField("owner_id")
    private Long ownerId;

    /**
    * 所属组织ID
    */
    @TableField("organization_id")
    private Long organizationId;

    /**
    * 封面图 URL
    */
    @TableField("cover_url")
    private String coverUrl;

    /**
    * 创建时间
    */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
    * 更新时间
    */
    @TableField("updated_at")
    private LocalDateTime updatedAt;


    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }
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
    public String getType() {
    return type;
    }

    public void setType(String type) {
    this.type = type;
    }
    public Long getOwnerId() {
    return ownerId;
    }

    public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
    }
    public Long getOrganizationId() {
    return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
    }
    public String getCoverUrl() {
    return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
    this.coverUrl = coverUrl;
    }
    public LocalDateTime getCreatedAt() {
    return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
    return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    }
}
