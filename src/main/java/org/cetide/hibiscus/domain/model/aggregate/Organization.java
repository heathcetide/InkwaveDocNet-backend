package org.cetide.hibiscus.domain.model.aggregate;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * Organization 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_organization")
public class Organization extends BaseEntity implements Serializable {

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
}
