package com.example.daobe.upload.presentation;

import com.example.daobe.upload.application.UploadService;
import com.example.daobe.upload.application.dto.UploadImageResponseDto;
import com.example.daobe.upload.application.dto.UploadImageUrlResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @Deprecated(since = "2024-10-21")
    @PostMapping("/images")
    public ResponseEntity<UploadImageResponseDto> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(uploadService.upload(file));
    }

    @GetMapping("/url")
    public ResponseEntity<UploadImageUrlResponseDto> uploadUrl() {
        return ResponseEntity.ok(uploadService.getUploadUrl());
    }
}
