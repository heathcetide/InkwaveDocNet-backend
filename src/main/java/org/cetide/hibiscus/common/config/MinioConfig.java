package org.cetide.hibiscus.common.config;

import io.minio.MinioClient;
import org.cetide.hibiscus.common.properties.MinioProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO 客户端配置类
 * MinioClient 是官方提供的 Java SDK 客户端，用于执行对象存储的各种操作（如上传、下载、删除等）。
 *
 * @author heathcetide
 */
@Configuration
public class MinioConfig {

    /**
     * 构造函数注入 Minio 配置信息
     */
    private final MinioProperties properties;

    public MinioConfig(MinioProperties properties) {
        this.properties = properties;
    }

    /**
     * 创建并配置 MinIO 客户端 Bean
     *
     * @return MinioClient 对象
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }
}