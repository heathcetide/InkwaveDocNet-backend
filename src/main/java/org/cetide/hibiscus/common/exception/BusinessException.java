package org.cetide.hibiscus.common.exception;

/**
 * 用户请求合法但由于业务规则无法执行，例如库存不足、余额不足
 *
 * @author heathcetide
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码，配合 ErrorCodeEnum 使用。
     */
    private final String errorCode;

    /**
     * 错误信息
     */
    private final String errorDetail;

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetail = null;
    }

    public BusinessException(String errorCode, String message, String errorDetail) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
    }

    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDetail = null;
    }

    public BusinessException(String errorCode, String message, String errorDetail, Throwable cause) {
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
        return "BusinessException{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + getMessage() + '\'' +
                ", errorDetail='" + errorDetail + '\'' +
                '}';
    }
}