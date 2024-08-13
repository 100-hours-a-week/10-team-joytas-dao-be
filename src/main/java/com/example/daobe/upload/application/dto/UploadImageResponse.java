package com.example.daobe.upload.application.dto;

public record UploadImageResponse(
        String image
) {

    public static UploadImageResponse of(String imageUrl) {
        return new UploadImageResponse(imageUrl);
    }
}
