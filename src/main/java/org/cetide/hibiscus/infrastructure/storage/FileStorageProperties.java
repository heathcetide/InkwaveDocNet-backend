package org.cetide.hibiscus.infrastructure.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hibiscus.storage")
public class FileStorageProperties {

    /**
     * 存储类型：local 或 minio
     */
    private String type;

    // MinIO
    private String minioEndpoint;
    private String minioAccessKey;
    private String minioSecretKey;
    private String minioBucket;

    // Local
    private String localBasePath;

    // COS 配置
    private String cosSecretId;
    private String cosSecretKey;
    private String cosRegion;
    private String cosBucket;
    private String cosUrlPrefix;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinioEndpoint() {
        return minioEndpoint;
    }

    public void setMinioEndpoint(String minioEndpoint) {
        this.minioEndpoint = minioEndpoint;
    }

    public String getMinioAccessKey() {
        return minioAccessKey;
    }

    public void setMinioAccessKey(String minioAccessKey) {
        this.minioAccessKey = minioAccessKey;
    }

    public String getMinioSecretKey() {
        return minioSecretKey;
    }

    public void setMinioSecretKey(String minioSecretKey) {
        this.minioSecretKey = minioSecretKey;
    }

    public String getMinioBucket() {
        return minioBucket;
    }

    public void setMinioBucket(String minioBucket) {
        this.minioBucket = minioBucket;
    }

    public String getLocalBasePath() {
        return localBasePath;
    }

    public void setLocalBasePath(String localBasePath) {
        this.localBasePath = localBasePath;
    }

    public String getCosSecretId() {
        return cosSecretId;
    }

    public void setCosSecretId(String cosSecretId) {
        this.cosSecretId = cosSecretId;
    }

    public String getCosSecretKey() {
        return cosSecretKey;
    }

    public void setCosSecretKey(String cosSecretKey) {
        this.cosSecretKey = cosSecretKey;
    }

    public String getCosRegion() {
        return cosRegion;
    }

    public void setCosRegion(String cosRegion) {
        this.cosRegion = cosRegion;
    }

    public String getCosBucket() {
        return cosBucket;
    }

    public void setCosBucket(String cosBucket) {
        this.cosBucket = cosBucket;
    }

    public String getCosUrlPrefix() {
        return cosUrlPrefix;
    }

    public void setCosUrlPrefix(String cosUrlPrefix) {
        this.cosUrlPrefix = cosUrlPrefix;
    }
}