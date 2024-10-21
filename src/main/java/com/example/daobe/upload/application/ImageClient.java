package com.example.daobe.upload.application;

import org.springframework.web.multipart.MultipartFile;

public interface ImageClient {

    String upload(String objectKey, MultipartFile file);

    String getUploadUrl(String objectKey);

    String getImageUrl(String objectKey);
}
