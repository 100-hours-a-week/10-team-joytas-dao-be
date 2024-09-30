package com.example.daobe.upload.infrastructure.s3;

import static com.example.daobe.upload.exception.UploadExceptionType.FILE_ENCODE_FAIL;
import static com.example.daobe.upload.exception.UploadExceptionType.UPLOAD_IMAGE_FAIL;

import com.example.daobe.upload.exception.UploadException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private static final String CACHE_CONTROL_PREFIX = "max-age=";

    private final S3Client s3Client;

    public void upload(int cacheTimeSeconds, String bucketName, String key, MultipartFile file) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .cacheControl(generateCacheControlHeader(cacheTimeSeconds))
                .build();
        putObject(request, file);
    }

    private String generateCacheControlHeader(int cacheTimeSeconds) {
        return CACHE_CONTROL_PREFIX + cacheTimeSeconds;
    }

    private void putObject(PutObjectRequest request, MultipartFile file) {
        try {
            s3Client.putObject(request, RequestBody.fromBytes(getBytes(file)));
        } catch (Exception ex) {
            throw new UploadException(UPLOAD_IMAGE_FAIL);
        }
    }

    private byte[] getBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException ex) {
            throw new UploadException(FILE_ENCODE_FAIL);
        }
    }
}
