package com.example.daobe.upload.application;

import com.example.daobe.upload.application.dto.UploadImageResponseDto;
import com.example.daobe.upload.application.dto.UploadImageUrlResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final ImageClient imageClient;

    @Deprecated(since = "2024-10-21")
    public UploadImageResponseDto upload(MultipartFile file) {
        String objectKey = UUID.randomUUID().toString();
        String imageUrl = imageClient.upload(objectKey, file);
        return UploadImageResponseDto.of(imageUrl);
    }

    public UploadImageUrlResponseDto getUploadUrl() {
        String objectKey = UUID.randomUUID().toString();
        String imageUrl = imageClient.getImageUrl(objectKey);
        String uploadUrl = imageClient.getUploadUrl(objectKey);
        return new UploadImageUrlResponseDto(imageUrl, uploadUrl);
    }
}
