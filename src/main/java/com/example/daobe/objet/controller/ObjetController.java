package com.example.daobe.objet.controller;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.objet.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.dto.ObjetCreateResponseDto;
import com.example.daobe.objet.service.ObjetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/objets")
@RequiredArgsConstructor
public class ObjetController {

    private final ObjetService objetService;

    @PostMapping
    public ResponseEntity<ApiResponse<ObjetCreateResponseDto>> generateObjet(
            @RequestBody ObjetCreateRequestDto request,
            @RequestPart(value = "objet_image", required = true) MultipartFile image) {
        ObjetCreateResponseDto ObjetCreateResponse = objetService.create(request);
        ApiResponse<ObjetCreateResponseDto> response = new ApiResponse<>("OBJET_CREATED_SUCCESS", ObjetCreateResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
