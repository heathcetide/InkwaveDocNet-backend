package org.cetide.hibiscus.domain.model.aggregate;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * Document 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_document")
public class Document extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId
    private Long id;

    /**
    * 所属知识库ID
    */
    @TableField("knowledge_base_id")
    private Long knowledgeBaseId;

    /**
    * 父文档ID（目录结构）
    */
    @TableField("parent_id")
    private Long parentId;

    /**
    * 文档标题
    */
    @TableField("title")
    private String title;

    /**
    * 类型（DOC / FOLDER）
    */
    @TableField("type")
    private String type;

    /**
    * 文档所有者ID
    */
    @TableField("owner_id")
    private Long ownerId;

    /**
    * 文档顺序排序值
    */
    @TableField("order")
    private Integer order;

    /**
    * 文档状态（ACTIVE / DELETED）
    */
    @TableField("status")
    private String status;

    /**
    * 当前版本号
    */
    @TableField("version")
    private Integer version;

    /**
    * 扩展元数据
    */
    @TableField("metadata")
    private String metadata;

    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }
    public Long getKnowledgeBaseId() {
    return knowledgeBaseId;
    }

    public void setKnowledgeBaseId(Long knowledgeBaseId) {
    this.knowledgeBaseId = knowledgeBaseId;
    }
    public Long getParentId() {
    return parentId;
    }

    public void setParentId(Long parentId) {
    this.parentId = parentId;
    }
    public String getTitle() {
    return title;
    }

    public void setTitle(String title) {
    this.title = title;
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
    public Integer getOrder() {
    return order;
    }

    public void setOrder(Integer order) {
    this.order = order;
    }
    public String getStatus() {
    return status;
    }

    public void setStatus(String status) {
    this.status = status;
    }
    public Integer getVersion() {
    return version;
    }

    public void setVersion(Integer version) {
    this.version = version;
    }
    public String getMetadata() {
    return metadata;
    }

    public void setMetadata(String metadata) {
    this.metadata = metadata;
    }
}
