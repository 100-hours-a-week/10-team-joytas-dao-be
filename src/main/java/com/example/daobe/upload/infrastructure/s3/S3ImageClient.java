package com.example.daobe.upload.infrastructure.s3;

import com.example.daobe.common.config.S3Properties;
import com.example.daobe.upload.application.ImageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3ImageClient implements ImageClient {

    private final S3Uploader s3Uploader;
    private final S3Properties properties;

    @Override
    public String upload(String objectKey, MultipartFile file) {
        String bucketName = properties.bucketName();
        int cacheTimeSeconds = properties.cacheTimeSeconds();
        s3Uploader.upload(cacheTimeSeconds, bucketName, objectKey, file);
        return generatedImageUrl(objectKey);
    }

    private String generatedImageUrl(String objectKey) {
        return properties.imageUrl() + objectKey;
    }
}
