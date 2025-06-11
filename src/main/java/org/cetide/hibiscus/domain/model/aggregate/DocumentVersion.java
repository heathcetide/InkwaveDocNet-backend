package org.cetide.hibiscus.domain.model.aggregate;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * DocumentVersion 实体类
 * @author Hibiscus-code-generate
 */
@TableName("hib_document_version")
public class DocumentVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @TableId
    private Long id;

    /**
    * 文档ID
    */
    @TableField("document_id")
    private Long documentId;

    /**
    * 版本号
    */
    @TableField("version")
    private Integer version;

    /**
    * 文档内容（富文本或 Markdown）
    */
    @TableField("content")
    private String content;

    /**
    * 编辑人ID
    */
    @TableField("editor_id")
    private Long editorId;

    /**
    * 创建时间
    */
    @TableField("created_at")
    private LocalDateTime createdAt;


    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }
    public Long getDocumentId() {
    return documentId;
    }

    public void setDocumentId(Long documentId) {
    this.documentId = documentId;
    }
    public Integer getVersion() {
    return version;
    }

    public void setVersion(Integer version) {
    this.version = version;
    }
    public String getContent() {
    return content;
    }

    public void setContent(String content) {
    this.content = content;
    }
    public Long getEditorId() {
    return editorId;
    }

    public void setEditorId(Long editorId) {
    this.editorId = editorId;
    }
    public LocalDateTime getCreatedAt() {
    return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    }
}
