package org.cetide.hibiscus.common.exception;

/**
 * 封装服务器内部错误，如数据库异常、第三方服务调用失败
 *
 * @author heathcetide
 */
public class SystemException extends RuntimeException {

    /**
     * 错误码，配合 ErrorCodeEnum 使用。
     */
    private final String errorCode;

    /**
     * 错误信息
     */
    private final String errorDetail;

    public SystemException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetail = null;
    }

    public SystemException(String errorCode, String message, String errorDetail) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
    }

    public SystemException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDetail = null;
    }

    public SystemException(String errorCode, String message, String errorDetail, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    @Override
    public String toString() {
        return "SystemException{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + getMessage() + '\'' +
                ", errorDetail='" + errorDetail + '\'' +
                ", cause='" + getCause() + '\'' +
                '}';
    }
}
