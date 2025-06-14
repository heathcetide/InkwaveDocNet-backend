package org.cetide.hibiscus.domain.model.aggregate;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * OrgInvite 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_org_invite")
public class OrgInvite extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
    * 邀请所属组织
    */
    @TableField("organization_id")
    private Long organizationId;

    /**
    * 发起邀请的用户ID
    */
    @TableField("inviter_id")
    private Long inviterId;

    /**
    * 邀请码（可转为二维码链接）
    */
    @TableField("invite_code")
    private String inviteCode;

    /**
    * 邀请加入的角色（MEMBER/ADMIN）
    */
    @TableField("role")
    private String role;

    /**
    * 最大可使用次数，null 为无限
    */
    @TableField("max_uses")
    private Integer maxUses;

    /**
    * 已使用次数
    */
    @TableField("used_count")
    private Integer usedCount;

    /**
    * 过期时间，可为空（永久有效）
    */
    @TableField("expires_at")
    private LocalDateTime expiresAt;

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
    public Long getInviterId() {
    return inviterId;
    }

    public void setInviterId(Long inviterId) {
    this.inviterId = inviterId;
    }
    public String getInviteCode() {
    return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
    this.inviteCode = inviteCode;
    }
    public String getRole() {
    return role;
    }

    public void setRole(String role) {
    this.role = role;
    }
    public Integer getMaxUses() {
    return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
    this.maxUses = maxUses;
    }
    public Integer getUsedCount() {
    return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
    this.usedCount = usedCount;
    }
    public LocalDateTime getExpiresAt() {
    return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
    }
}
