package com.example.daobe.upload.application.dto;

public record UploadImageResponseDto(
        String imageUrl
) {

    public static UploadImageResponseDto of(String imageUrl) {
        return new UploadImageResponseDto(imageUrl);
    }
}
