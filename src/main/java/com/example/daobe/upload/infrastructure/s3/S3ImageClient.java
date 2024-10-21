package com.example.daobe.upload.infrastructure.s3;

import com.example.daobe.common.config.S3Properties;
import com.example.daobe.upload.application.ImageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3ImageClient implements ImageClient {

    private static final String CACHE_CONTROL_PREFIX = "max-age=";

    private final S3Uploader s3Uploader;
    private final S3Properties properties;
    private final S3PresignHandler s3PresignedUrlGenerator;

    @Override
    public String upload(String objectKey, MultipartFile file) {
        String bucketName = properties.bucketName();
        int cacheTimeSeconds = properties.cacheTimeSeconds();
        s3Uploader.upload(cacheTimeSeconds, bucketName, objectKey, file);
        return getImageUrl(objectKey);
    }

    @Override
    public String getUploadUrl(String objectKey) {
        PutObjectRequest request = generateRequest(objectKey);
        return s3PresignedUrlGenerator.execute(request);
    }

    @Override
    public String getImageUrl(String objectKey) {
        return properties.imageUrl() + objectKey;
    }

    private PutObjectRequest generateRequest(String objectKey) {
        return PutObjectRequest.builder()
                .bucket(properties.bucketName())
                .key(objectKey)
                .cacheControl(CACHE_CONTROL_PREFIX + properties.cacheTimeSeconds())
                .build();
    }
}
