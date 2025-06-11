package org.cetide.hibiscus.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.cetide.hibiscus.domain.model.enums.ResponseCodeEnum;

/**
 * 统一请求响应类
 *
 * @author heathcetide
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(ResponseCodeEnum.SUCCESS.code());
        response.setMessage(ResponseCodeEnum.SUCCESS.message());
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(ResponseCodeEnum.SYSTEM_ERROR.code(), message);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return error(ResponseCodeEnum.NOT_FOUND.code(), message);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    // 添加链式调用方法
    public ApiResponse<T> code(String code) {
        this.code = code;
        return this;
    }

    public ApiResponse<T> message(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
} 