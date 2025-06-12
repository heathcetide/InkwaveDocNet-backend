package org.cetide.hibiscus.infrastructure.storage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Autowired
    private MinioStorageService minioStorageService;

    @Autowired
    private LocalStorageService localStorageService;

    @Autowired
    private FileStorageProperties properties;

    @Bean
    public FileStorageAdapter fileStorageAdapter() {
        return switch (properties.getType()) {
            case "minio" -> minioStorageService;
            case "local" -> localStorageService;
            default -> throw new IllegalArgumentException("不支持的存储类型: " + properties.getType());
        };
    }
}