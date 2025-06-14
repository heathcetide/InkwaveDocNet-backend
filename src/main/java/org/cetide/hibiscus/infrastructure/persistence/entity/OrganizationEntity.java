package org.cetide.hibiscus.infrastructure.persistence.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cetide.hibiscus.domain.model.aggregate.BaseEntity;

import java.io.Serializable;

/**
 * Organization 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_organization")
public class OrganizationEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 组织名称（唯一）
     */
    @TableField("name")
    private String name;

    /**
     * 组织描述
     */
    @TableField("description")
    private String description;

    /**
     * 组织拥有者（用户ID）
     */
    @TableField("owner_id")
    private Long ownerId;

    /**
     * 组织状态
     */
    @TableField("status")
    private String status;

    /**
     * 组织是否公开
     */
    @TableField("published")
    private Boolean published;

    /**
     * 组织创建时间
     */
    @TableField("max_members")
    private Integer maxMembers;

    /**
     * 组织当前成员数
     */
    @TableField("current_members")
    private Integer currentMembers;

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
    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }

    public Integer getCurrentMembers() {
        return currentMembers;
    }

    public void setCurrentMembers(Integer currentMembers) {
        this.currentMembers = currentMembers;
    }
}
