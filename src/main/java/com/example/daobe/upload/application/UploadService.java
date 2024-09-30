package com.example.daobe.upload.application;

import com.example.daobe.upload.application.dto.UploadImageResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final ImageClient imageClient;

    public UploadImageResponseDto upload(MultipartFile file) {
        String objectKey = UUID.randomUUID().toString();
        String imageUrl = imageClient.upload(objectKey, file);
        return UploadImageResponseDto.of(imageUrl);
    }
}
