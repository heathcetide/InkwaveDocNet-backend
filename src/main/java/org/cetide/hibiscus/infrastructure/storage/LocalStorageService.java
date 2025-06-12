package org.cetide.hibiscus.infrastructure.storage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalStorageService implements FileStorageAdapter {

    @Autowired
    private FileStorageProperties properties;

    @Override
    public String upload(MultipartFile file) {
        try {
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(properties.getLocalBasePath(), filename);
            Files.createDirectories(path.getParent());
            file.transferTo(path.toFile());
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("本地文件上传失败", e);
        }
    }

    @Override
    public byte[] download(String path) {
        try {
            Path filePath = Paths.get(properties.getLocalBasePath(), path);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("本地文件下载失败", e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            Path filePath = Paths.get(properties.getLocalBasePath(), path);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("本地文件删除失败", e);
        }
    }
}
