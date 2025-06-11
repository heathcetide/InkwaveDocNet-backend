package org.cetide.hibiscus.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MinIO 属性配置类，用于将配置文件中的 minio.* 信息映射为 Java 对象。
 * 使用 @ConfigurationProperties 可自动从 application.yml 或 application.properties 中读取配置。
 * 例如:
 * minio:
 *   endpoint: <a href="http://localhost:9000">...</a>
 *   access-key: dominion
 *   secret-key: dominion。
 *
 * @author heathcetide
 */
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * MinIO 服务的地址，例如：<a href="http://localhost:9000">...</a>
     */
    private String endpoint;

    /**
     * 访问 MinIO 的 Access Key（用户名）
     */
    private String accessKey;

    /**
     * 访问 MinIO 的 Secret Key（密码）
     */
    private String secretKey;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}