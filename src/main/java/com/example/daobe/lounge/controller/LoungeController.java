package com.example.daobe.lounge.controller;

import static com.example.daobe.lounge.entity.LoungeResult.LOUNGE_CREATED_SUCCESS;
import static com.example.daobe.lounge.entity.LoungeResult.LOUNGE_DELETED_SUCCESS;
import static com.example.daobe.lounge.entity.LoungeResult.LOUNGE_INFO_LOADED_SUCCESS;
import static com.example.daobe.lounge.entity.LoungeResult.LOUNGE_LIST_LOADED_SUCCESS;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.lounge.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.dto.LoungeDetailInfoDto;
import com.example.daobe.lounge.dto.LoungeInfoDto;
import com.example.daobe.lounge.dto.LoungeInviteDto;
import com.example.daobe.lounge.entity.LoungeResult;
import com.example.daobe.lounge.service.LoungeFacadeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<ApiResponse<LoungeCreateResponseDto>> createLounge(
            @AuthenticationPrincipal Long userId,
            @RequestBody LoungeCreateRequestDto request
    ) {
        LoungeCreateResponseDto response = loungeFacadeService.create(request, userId);
        return ResponseEntity.status(LOUNGE_CREATED_SUCCESS.getHttpStatus())
                .body(new ApiResponse<>(LOUNGE_CREATED_SUCCESS.getMessage(), response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LoungeInfoDto>>> getAllLounges(
            @AuthenticationPrincipal Long userId
    ) {
        ApiResponse<List<LoungeInfoDto>> response = new ApiResponse<>(
                LOUNGE_LIST_LOADED_SUCCESS.getMessage(),
                loungeFacadeService.findLoungeByUserId(userId)
        );
        return ResponseEntity.status(LOUNGE_LIST_LOADED_SUCCESS.getHttpStatus()).body(response);
    }

    @GetMapping("/{loungeId}")
    public ResponseEntity<ApiResponse<LoungeDetailInfoDto>> getLoungeDetail(
            @PathVariable(name = "loungeId") Long loungeId
    ) {
        ApiResponse<LoungeDetailInfoDto> response = new ApiResponse<>(
                LOUNGE_INFO_LOADED_SUCCESS.getMessage(),
                loungeFacadeService.getLoungeDetail(loungeId)
        );
        return ResponseEntity.status(LOUNGE_INFO_LOADED_SUCCESS.getHttpStatus()).body(response);
    }

    @DeleteMapping("/{loungeId}")
    public ResponseEntity<ApiResponse<LoungeInfoDto>> deleteLounge(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "loungeId") Long loungeId
    ) {
        ApiResponse<LoungeInfoDto> response = new ApiResponse<>(
                LOUNGE_DELETED_SUCCESS.getMessage(),
                loungeFacadeService.delete(userId, loungeId)
        );
        return ResponseEntity.status(LOUNGE_DELETED_SUCCESS.getHttpStatus()).body(response);
    }

    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<String>> inviteUser(
            @RequestBody LoungeInviteDto request
    ) {
        LoungeResult inviteResult = loungeFacadeService.inviteUser(request);
        ApiResponse<String> response = new ApiResponse<>(
                inviteResult.getMessage(),
                null
        );
        return ResponseEntity.status(inviteResult.getHttpStatus()).body(response);
    }
}
