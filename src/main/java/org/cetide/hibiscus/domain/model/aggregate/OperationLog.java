package org.cetide.hibiscus.domain.model.aggregate;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cetide.hibiscus.domain.model.enums.LogType;
import org.cetide.hibiscus.domain.model.valueobject.Description;
import org.cetide.hibiscus.domain.model.valueobject.Operator;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统操作日志
 *
 * @author heathcetide
 */
@TableName("operation_log")
public class OperationLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    @TableField("type")
    private LogType type;

    @TableField("description")
    private Description description;

    @TableField("operator")
    private Operator operator;

    @TableField("user_id")
    private Long userId;

    @TableField("success")
    private boolean success;

    @TableField("params")
    private String params;

    @TableField("result")
    private String result;

    @TableField("timestamp")
    private Date timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}