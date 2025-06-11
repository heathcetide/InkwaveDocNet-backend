package org.cetide.hibiscus.domain.model.valueobject;

public class Operator {

    private final String name;

    public Operator(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("操作人不能为空");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("操作人名称过长");
        }
        this.name = name;
    }

    public String getValue() {
        return name;
    }
}