package org.cetide.hibiscus.common.exception;

/**
 * 资源没有找到异常
 *
 * @author heathcetide
 */
public class NotFoundException extends RuntimeException {

    /**
     * 错误码，配合 ErrorCodeEnum 使用。
     */
    private final String errorCode;

    /**
     * 错误信息
     */
    private final String errorDetail;

    public NotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetail = null;
    }

    public NotFoundException(String errorCode, String message, String errorDetail) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
    }

    public NotFoundException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDetail = null;
    }

    public NotFoundException(String errorCode, String message, String errorDetail, Throwable cause) {
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
        return "NotFoundException{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + getMessage() + '\'' +
                ", errorDetail='" + errorDetail + '\'' +
                '}';
    }
}
