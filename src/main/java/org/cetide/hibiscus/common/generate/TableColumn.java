package org.cetide.hibiscus.common.generate;

import java.util.Objects;

public class TableColumn {

    private String name;
    private String type;
    private String comment;

    private boolean nullable;
    private boolean primaryKey;

    private int length;
    private int precision;
    private int scale;

    private boolean indexed;       // 是否被索引
    private boolean unique;        // 是否是唯一索引
    private String indexName;      // 索引名（如果属于某个索引）

    private String fullType;

    private String fieldName;       // 最终用于代码生成的字段名（可驼峰或保留下划线）

    private String originalName;    // 原始列名（COLUMN_NAME）


    public TableColumn() {
    }

    public TableColumn(String name, String type, String comment) {
        this.name = name;
        this.type = type;
        this.comment = comment;
    }

    // 全字段构造器（可选）
    public TableColumn(String name, String type, String comment,
                       boolean nullable, boolean primaryKey,
                       int length, int precision, int scale,
                       boolean indexed, boolean unique, String indexName) {
        this.name = name;
        this.type = type;
        this.comment = comment;
        this.nullable = nullable;
        this.primaryKey = primaryKey;
        this.length = length;
        this.precision = precision;
        this.scale = scale;
        this.indexed = indexed;
        this.unique = unique;
        this.indexName = indexName;
    }

    // Getter / Setter（新增部分）
    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getFullType() {
        return fullType;
    }

    public void setFullType(String fullType) {
        this.fullType = fullType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Override
    public String toString() {
        return "TableColumn{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                ", nullable=" + nullable +
                ", primaryKey=" + primaryKey +
                ", length=" + length +
                ", precision=" + precision +
                ", scale=" + scale +
                ", indexed=" + indexed +
                ", unique=" + unique +
                ", indexName='" + indexName + '\'' +
                ", fullType='" + fullType + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", originalName='" + originalName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableColumn)) return false;
        TableColumn that = (TableColumn) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
