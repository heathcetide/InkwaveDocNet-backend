package org.cetide.hibiscus.domain.model.valueobject;

public class Description {

    private final String value;

    public Description(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("操作描述不能为空");
        }
        if (value.length() > 200) {
            throw new IllegalArgumentException("操作描述不能超过200个字符");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
