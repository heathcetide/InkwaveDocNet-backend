package org.cetide.hibiscus.infrastructure.storage;


import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioStorageService implements FileStorageAdapter {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private FileStorageProperties properties;

    @Override
    public String upload(MultipartFile file) {
        try {
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.getMinioBucket())
                            .object(filename)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return filename;
        } catch (Exception e) {
            throw new RuntimeException("MinIO上传失败", e);
        }
    }

    @Override
    public byte[] download(String path) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(properties.getMinioBucket())
                        .object(path)
                        .build())) {
            return stream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("MinIO下载失败", e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(properties.getMinioBucket())
                            .object(path)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("MinIO删除失败", e);
        }
    }
}