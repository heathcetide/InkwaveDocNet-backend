package org.cetide.hibiscus.domain.model.aggregate;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * User 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_user")
public class User extends BaseEntity implements Serializable {

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

    public User(){

    }

    public User(Long id, String username, String email, String password, String avatarUrl, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.status = status;
    }

    public User(Long id, String username, String email, String password,
                String avatarUrl, String status,
                LocalDateTime createdAt, LocalDateTime updatedAt,
                boolean deleted) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.status = status;
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
        this.setDeleted(deleted);
    }

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

    public void update(String username, String email, String avatarUrl, String status) {
        if (username != null) this.username = username;
        if (email != null) this.email = email;
        if (avatarUrl != null) this.avatarUrl = avatarUrl;
        if (status != null) this.status = status;
        this.setUpdatedAt(LocalDateTime.now());
    }

    public void markDeleted() {
        this.setDeleted(true);
        this.status = "DELETED";
        this.setUpdatedAt(LocalDateTime.now());
    }
}
