package com.example.daobe.lounge.presentation;

import static com.example.daobe.lounge.domain.LoungeResult.LOUNGE_CREATED_SUCCESS;
import static com.example.daobe.lounge.domain.LoungeResult.LOUNGE_DELETED_SUCCESS;
import static com.example.daobe.lounge.domain.LoungeResult.LOUNGE_INFO_LOADED_SUCCESS;
import static com.example.daobe.lounge.domain.LoungeResult.LOUNGE_LIST_LOADED_SUCCESS;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.lounge.application.LoungeFacadeService;
import com.example.daobe.lounge.application.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.application.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.application.dto.LoungeDetailInfoDto;
import com.example.daobe.lounge.application.dto.LoungeInfoDto;
import com.example.daobe.lounge.application.dto.LoungeInviteDto;
import com.example.daobe.lounge.domain.LoungeResult;
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
                .body(new ApiResponse<>(LOUNGE_CREATED_SUCCESS.name(), response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LoungeInfoDto>>> getAllLounges(
            @AuthenticationPrincipal Long userId
    ) {
        ApiResponse<List<LoungeInfoDto>> response = new ApiResponse<>(
                LOUNGE_LIST_LOADED_SUCCESS.name(),
                loungeFacadeService.findLoungeByUserId(userId)
        );
        return ResponseEntity.status(LOUNGE_LIST_LOADED_SUCCESS.getHttpStatus()).body(response);
    }

    @GetMapping("/{loungeId}")
    public ResponseEntity<ApiResponse<LoungeDetailInfoDto>> getLoungeDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "loungeId") Long loungeId
    ) {
        ApiResponse<LoungeDetailInfoDto> response = new ApiResponse<>(
                LOUNGE_INFO_LOADED_SUCCESS.name(),
                loungeFacadeService.getLoungeDetail(userId, loungeId)
        );
        return ResponseEntity.status(LOUNGE_INFO_LOADED_SUCCESS.getHttpStatus()).body(response);
    }

    @DeleteMapping("/{loungeId}")
    public ResponseEntity<ApiResponse<LoungeInfoDto>> deleteLounge(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "loungeId") Long loungeId
    ) {
        loungeFacadeService.delete(userId, loungeId);
        return ResponseEntity.status(LOUNGE_DELETED_SUCCESS.getHttpStatus()).body(null);
    }

    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<String>> inviteUser(
            @RequestBody LoungeInviteDto request
    ) {
        LoungeResult inviteResult = loungeFacadeService.inviteUser(request);
        ApiResponse<String> response = new ApiResponse<>(
                inviteResult.name(),
                null
        );
        return ResponseEntity.status(inviteResult.getHttpStatus()).body(response);
    }
}
