package com.example.daobe.upload.s3;

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
        s3Uploader.upload(bucketName, objectKey, file);
        return generatedImageUrl(objectKey);
    }

    // image 가 저장된 URL 생성
    private String generatedImageUrl(String objectKey) {
        return properties.imageUrl() + objectKey;
    }
}
