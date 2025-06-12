package org.cetide.hibiscus.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageAdapter {
    String upload(MultipartFile file);

    byte[] download(String path);

    void delete(String path);
}