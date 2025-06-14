package org.cetide.hibiscus.infrastructure.storage;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class CosStorageService implements FileStorageAdapter {

    @Autowired
    private FileStorageProperties properties;

    private COSClient buildClient() {
        COSCredentials credentials = new BasicCOSCredentials(
                properties.getCosSecretId(), properties.getCosSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(properties.getCosRegion()));
        return new COSClient(credentials, clientConfig);
    }

    @Override
    public String upload(MultipartFile file) {
        COSClient cosClient = null;
        try {
            cosClient = buildClient();
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            PutObjectRequest request = new PutObjectRequest(
                    properties.getCosBucket(), filename, file.getInputStream(), metadata);
            request.setCannedAcl(CannedAccessControlList.PublicRead);

            cosClient.putObject(request);

            return filename; // 或拼接返回完整 URL

        } catch (Exception e) {
            throw new RuntimeException("COS 上传失败", e);
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    @Override
    public byte[] download(String path) {
        COSClient cosClient = null;
        try {
            cosClient = buildClient();

            try (InputStream inputStream = cosClient.getObject(properties.getCosBucket(), path).getObjectContent();
                 ByteArrayOutputStream output = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[8192];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, len);
                }
                return output.toByteArray();
            }

        } catch (Exception e) {
            throw new RuntimeException("COS 下载失败", e);
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    @Override
    public void delete(String path) {
        COSClient cosClient = null;
        try {
            cosClient = buildClient();
            cosClient.deleteObject(properties.getCosBucket(), path);
        } catch (Exception e) {
            throw new RuntimeException("COS 删除失败", e);
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }
}