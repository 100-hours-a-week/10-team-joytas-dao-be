package com.example.daobe.lounge.controller;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.lounge.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.service.LoungeFacadeService;
import com.example.daobe.lounge.service.LoungeService;
import com.example.daobe.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/lounges")
@RequiredArgsConstructor
public class LoungeController {

    private final LoungeFacadeService loungeFacadeService;

    // TODO: ApiResponse 응답 메시지 Enum 으로 관리 하도록 구현
    @PostMapping
    public ResponseEntity<ApiResponse<LoungeCreateResponseDto>> generateLounge(
            @RequestBody LoungeCreateRequestDto request
    ) {
        ApiResponse<LoungeCreateResponseDto> response = new ApiResponse<>(
                "LOUNGE_CREATED_SUCCESS",
                loungeFacadeService.create(request)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
