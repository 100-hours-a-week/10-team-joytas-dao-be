package com.example.daobe.upload.application;

import com.example.daobe.upload.application.dto.UploadImageResponse;
import com.example.daobe.upload.application.dto.UploadImageResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final ImageClient imageClient;

    @Deprecated(since = "2024-08-29")
    public UploadImageResponse uploadImage(MultipartFile file) {
        String objectKey = UUID.randomUUID().toString();
        String imageUrl = imageClient.upload(objectKey, file);
        return UploadImageResponse.of(imageUrl);
    }

    public UploadImageResponseDto upload(MultipartFile file) {
        String objectKey = UUID.randomUUID().toString();
        String imageUrl = imageClient.upload(objectKey, file);
        return UploadImageResponseDto.of(imageUrl);
    }
}
