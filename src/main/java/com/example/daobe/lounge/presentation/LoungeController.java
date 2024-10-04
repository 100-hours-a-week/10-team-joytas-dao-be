package com.example.daobe.lounge.presentation;

import static com.example.daobe.lounge.presentation.support.LoungeResultType.LOUNGE_CREATED_SUCCESS;
import static com.example.daobe.lounge.presentation.support.LoungeResultType.LOUNGE_DELETED_SUCCESS;
import static com.example.daobe.lounge.presentation.support.LoungeResultType.LOUNGE_INFO_LOADED_SUCCESS;
import static com.example.daobe.lounge.presentation.support.LoungeResultType.LOUNGE_INVITE_ACCEPTED_SUCCESS;
import static com.example.daobe.lounge.presentation.support.LoungeResultType.LOUNGE_INVITE_SUCCESS;
import static com.example.daobe.lounge.presentation.support.LoungeResultType.LOUNGE_LIST_LOADED_SUCCESS;
import static com.example.daobe.lounge.presentation.support.LoungeResultType.LOUNGE_SHARER_INFO_LOADED_SUCCESS;
import static com.example.daobe.lounge.presentation.support.LoungeResultType.LOUNGE_WITHDRAW_SUCCESS;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.lounge.application.LoungeFacadeService;
import com.example.daobe.lounge.application.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.application.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.application.dto.LoungeDetailInfoDto;
import com.example.daobe.lounge.application.dto.LoungeInfoDto;
import com.example.daobe.lounge.application.dto.LoungeInviteDto;
import com.example.daobe.lounge.application.dto.LoungeSharerInfoResponseDto;
import com.example.daobe.lounge.application.dto.LoungeValidateRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lounges")
@RequiredArgsConstructor
public class LoungeController {

    private final LoungeFacadeService loungeFacadeService;

    @PostMapping
    public ResponseEntity<ApiResponse<LoungeCreateResponseDto>> createLounge(
            @AuthenticationPrincipal Long userId,
            @RequestBody LoungeCreateRequestDto request
    ) {
        LoungeCreateResponseDto response = loungeFacadeService.createLounge(request, userId);
        return ResponseEntity.status(LOUNGE_CREATED_SUCCESS.getHttpStatus())
                .body(new ApiResponse<>(
                        LOUNGE_CREATED_SUCCESS.name(),
                        response)
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LoungeInfoDto>>> getAllLounges(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.status(LOUNGE_LIST_LOADED_SUCCESS.getHttpStatus())
                .body(new ApiResponse<>(
                        LOUNGE_LIST_LOADED_SUCCESS.name(),
                        loungeFacadeService.getAllLounges(userId))
                );
    }

    @GetMapping("/{loungeId}")
    public ResponseEntity<ApiResponse<LoungeDetailInfoDto>> getLoungeDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "loungeId") Long loungeId
    ) {
        return ResponseEntity.status(LOUNGE_INFO_LOADED_SUCCESS.getHttpStatus())
                .body(new ApiResponse<>(
                        LOUNGE_INFO_LOADED_SUCCESS.name(),
                        loungeFacadeService.getLoungeDetail(userId, loungeId))
                );
    }

    @DeleteMapping("/{loungeId}")
    public ResponseEntity<ApiResponse<Void>> deleteLounge(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long loungeId
    ) {
        loungeFacadeService.deleteLounge(userId, loungeId);
        return ResponseEntity.status(LOUNGE_DELETED_SUCCESS.getHttpStatus())
                .body(new ApiResponse<>(
                        LOUNGE_DELETED_SUCCESS.name(),
                        null)
                );
    }

    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<Void>> inviteUser(
            @AuthenticationPrincipal Long userId,
            @RequestBody LoungeInviteDto request
    ) {
        loungeFacadeService.inviteUser(request, userId);
        return ResponseEntity.status(LOUNGE_INVITE_SUCCESS.getHttpStatus())
                .body(new ApiResponse<>(
                        LOUNGE_INVITE_SUCCESS.name(),
                        null)
                );
    }

    @PatchMapping("/{loungeId}/invite/accept")
    public ResponseEntity<ApiResponse<Void>> updateInvitedUserStatus(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long loungeId
    ) {
        loungeFacadeService.updateInvitedUserStatus(userId, loungeId);
        return ResponseEntity.status(LOUNGE_INVITE_SUCCESS.getHttpStatus())
                .body(new ApiResponse<>(
                        LOUNGE_INVITE_ACCEPTED_SUCCESS.name(),
                        null)
                );
    }

    @GetMapping("/{loungeId}/search")
    public ResponseEntity<ApiResponse<List<LoungeSharerInfoResponseDto>>> searchLoungeSharer(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long loungeId,
            @RequestParam(value = "nickname", defaultValue = "") String nickname
    ) {
        return ResponseEntity.status(LOUNGE_SHARER_INFO_LOADED_SUCCESS.getHttpStatus())
                .body(new ApiResponse<>(
                        LOUNGE_SHARER_INFO_LOADED_SUCCESS.name(),
                        loungeFacadeService.searchLoungeSharer(userId, nickname, loungeId)
                ));
    }

    @PostMapping("/{loungeId}/withdraw")
    public ResponseEntity<ApiResponse<Void>> withDraw(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long loungeId
    ) {
        loungeFacadeService.withdraw(userId, loungeId);
        return ResponseEntity.status(LOUNGE_WITHDRAW_SUCCESS.getHttpStatus())
                .body(new ApiResponse<>(
                        LOUNGE_WITHDRAW_SUCCESS.name(),
                        null
                ));
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Void>> validate(
            @AuthenticationPrincipal Long userId,
            @RequestBody LoungeValidateRequestDto request
    ) {
        loungeFacadeService.isLoungeSharer(userId, request);
        return ResponseEntity.ok(new ApiResponse<>(
                "AUTHENTICATION_SUCCESS",
                null
        ));
    }
}
