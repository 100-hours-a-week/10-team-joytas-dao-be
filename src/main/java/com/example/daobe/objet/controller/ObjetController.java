package com.example.daobe.objet.controller;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.objet.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.dto.ObjetCreateResponseDto;
import com.example.daobe.objet.dto.ObjetDetailInfoDto;
import com.example.daobe.objet.dto.ObjetInfoDto;
import com.example.daobe.objet.service.ObjetService;
import com.example.daobe.upload.application.UploadService;
import com.example.daobe.upload.application.dto.UploadImageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/objets")
@RequiredArgsConstructor
public class ObjetController {

    private static final String OBJET_CREATED_SUCCESS = "OBJET_CREATED_SUCCESS";

    private final ObjetService objetService;
    private final UploadService uploadService;

    @PostMapping
    public ResponseEntity<ApiResponse<ObjetCreateResponseDto>> generateObjet(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("type") String type,
            @RequestParam("lounge_id") Long loungeId,
            @RequestParam("owners") List<Long> owners,
            @RequestParam("objet_image") MultipartFile file
    ) {
        UploadImageResponse uploadImageResponse = uploadService.uploadImage(file);

        ObjetCreateRequestDto request = new ObjetCreateRequestDto(owners, name, description, type, loungeId);

        ObjetCreateResponseDto ObjetCreateResponse = objetService.create(request, uploadImageResponse.image());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(OBJET_CREATED_SUCCESS, ObjetCreateResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ObjetInfoDto>>> getAllObjets(
            @RequestParam Long lounge_id, @RequestParam Boolean owner
    ) {
        ApiResponse<List<ObjetInfoDto>> response = new ApiResponse<>(
                "OBJET_LIST_LOADED_SUCCESS", objetService.getObjetList(lounge_id, owner)
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{objetId}")
    public ResponseEntity<ApiResponse<ObjetDetailInfoDto>> getObjetDetail(
            @PathVariable(name = "objetId") Long objetId
    ) {
        ApiResponse<ObjetDetailInfoDto> response = new ApiResponse<>(
                "OBJET_DETAIL_LOADED_SUCCESS", objetService.getObjetDetail(objetId)
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
