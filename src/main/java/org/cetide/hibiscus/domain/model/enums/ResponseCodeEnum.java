package org.cetide.hibiscus.domain.model.enums;

public enum ResponseCodeEnum {

    // ========== 成功响应 ==========
    SUCCESS("SUCCESS", "请求成功"),

    // ========== 通用系统错误 ==========
    SYSTEM_ERROR("SYS_0001", "系统内部错误"),
    DB_ERROR("SYS_0002", "数据库异常"),
    SERVICE_UNAVAILABLE("SYS_0003", "服务不可用"),
    THIRD_PARTY_ERROR("SYS_0004", "第三方服务调用失败"),
    // 限流
    RATE_LIMIT_EXCEEDED("SYS_0005", "请求频率过高"),

    // ========== 参数与校验相关 ==========
    VALIDATION_ERROR("VAL_1001", "参数校验失败"),
    MISSING_PARAMETER("VAL_1002", "缺少必要参数"),
    ILLEGAL_ARGUMENT("VAL_1003", "参数不合法"),

    // ========== 认证与权限 ==========
    UNAUTHORIZED("AUTH_2001", "用户未登录或Token无效"),
    FORBIDDEN("AUTH_2002", "没有权限访问资源"),

    // ========== 业务逻辑错误 ==========
    BUSINESS_ERROR("BUS_3001", "业务处理异常"),
    INSUFFICIENT_BALANCE("BUS_3002", "余额不足"),
    ITEM_OUT_OF_STOCK("BUS_3003", "商品库存不足"),

    // ========== 资源相关 ==========
    NOT_FOUND("RES_4001", "资源不存在"),
    DUPLICATE_RESOURCE("RES_4002", "资源已存在");

    private final String code;
    private final String message;

    ResponseCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return code + ": " + message;
    }
}
