package org.cetide.hibiscus.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cetide.hibiscus.domain.model.aggregate.BaseEntity;

import java.io.Serializable;

@TableName("hib_user")
public class UserEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 用户名（唯一）
     */
    @TableField("username")
    private String username;

    /**
     * 电子邮箱（可选唯一）
     */
    @TableField("email")
    private String email;

    /**
     * 加密后的密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户头像 URL
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 用户状态（ACTIVE / BANNED / DELETED）
     */
    @TableField("status")
    private String status;

    /**
     * 主题（LIGHT / DARK）
     */
    @TableField("theme_dark")
    private Boolean themeDark;

    /**
     * 邮箱通知（true / false）
     */
    @TableField("email_notifications")
    private Boolean emailNotifications;

    /**
     * 语言（EN / ZH）
     */
    @TableField("language")
    private String language;

    /**
     * 个性签名
     */
    @TableField("bio")
    private String bio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getThemeDark() {
        return themeDark;
    }

    public void setThemeDark(Boolean themeDark) {
        this.themeDark = themeDark;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", status='" + status + '\'' +
                ", themeDark=" + themeDark +
                ", emailNotifications=" + emailNotifications +
                ", language='" + language + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}