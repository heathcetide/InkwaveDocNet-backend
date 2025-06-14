package org.cetide.hibiscus.infrastructure.storage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {


    private final MinioStorageService minioStorageService;

    private final LocalStorageService localStorageService;

    private final CosStorageService cosStorageService;

    private final FileStorageProperties properties;

    public StorageConfig(MinioStorageService minioStorageService, LocalStorageService localStorageService, CosStorageService cosStorageService, FileStorageProperties properties) {
        this.minioStorageService = minioStorageService;
        this.localStorageService = localStorageService;
        this.cosStorageService = cosStorageService;
        this.properties = properties;
    }

    @Bean
    public FileStorageAdapter fileStorageAdapter() {
        return switch (properties.getType()) {
            case "minio" -> minioStorageService;
            case "local" -> localStorageService;
            case "cos" -> cosStorageService;
            default -> throw new IllegalArgumentException("不支持的存储类型: " + properties.getType());
        };
    }
}