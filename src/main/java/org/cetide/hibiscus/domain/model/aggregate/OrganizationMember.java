package org.cetide.hibiscus.domain.model.aggregate;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * OrganizationMember 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_organization_member")
public class OrganizationMember extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId
    private Long id;

    /**
    * 组织ID
    */
    @TableField("organization_id")
    private Long organizationId;

    /**
    * 用户ID
    */
    @TableField("user_id")
    private Long userId;

    /**
    * 成员角色（OWNER / ADMIN / MEMBER）
    */
    @TableField("role")
    private String role;

    /**
    * 状态（ACTIVE / INVITED / REMOVED）
    */
    @TableField("status")
    private String status;

    /**
    * 加入时间
    */
    @TableField("joined_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinedAt;


    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }
    public Long getOrganizationId() {
    return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
    }
    public Long getUserId() {
    return userId;
    }

    public void setUserId(Long userId) {
    this.userId = userId;
    }
    public String getRole() {
    return role;
    }

    public void setRole(String role) {
    this.role = role;
    }
    public String getStatus() {
    return status;
    }

    public void setStatus(String status) {
    this.status = status;
    }
    public LocalDateTime getJoinedAt() {
    return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
    this.joinedAt = joinedAt;
    }
}
