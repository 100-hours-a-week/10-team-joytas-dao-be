package com.example.daobe.upload.application;

import com.example.daobe.upload.application.dto.UploadImageResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final ImageClient imageClient;

    public UploadImageResponse uploadImage(MultipartFile file) {
        String objectKey = UUID.randomUUID().toString();
        String imageUrl = imageClient.upload(objectKey, file);
        return UploadImageResponse.of(imageUrl);
    }
}
