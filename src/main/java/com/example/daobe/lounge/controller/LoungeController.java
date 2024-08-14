package com.example.daobe.lounge.controller;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.lounge.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.dto.LoungeDetailInfoDto;
import com.example.daobe.lounge.dto.LoungeInfoDto;
import com.example.daobe.lounge.dto.LoungeInviteDto;
import com.example.daobe.lounge.entity.InviteStatus;
import com.example.daobe.lounge.service.LoungeFacadeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @RequestBody LoungeCreateRequestDto request
    ) {
        ApiResponse<LoungeCreateResponseDto> response = new ApiResponse<>(
                "LOUNGE_CREATED_SUCCESS",
                loungeFacadeService.create(request)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // TODO: @AuthenticationPrincipal 적용 -> 유저 인증
    @GetMapping
    public ResponseEntity<ApiResponse<List<LoungeInfoDto>>> getAllLounges() {
        ApiResponse<List<LoungeInfoDto>> response = new ApiResponse<>(
                "LOUNGE_LIST_LOADED_SUCCESS",
                loungeFacadeService.findLoungeByUserId(2L)  // FIXME: Security 적용 후 userId로 변경
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{loungeId}")
    public ResponseEntity<ApiResponse<LoungeDetailInfoDto>> getLoungeDetail(
            @PathVariable(name = "loungeId") Long loungeId
    ) {
        ApiResponse<LoungeDetailInfoDto> response = new ApiResponse<>(
                "LOUNGE_INFO_LOADED_SUCCESS",
                loungeFacadeService.getLoungeDetail(loungeId)
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<LoungeInviteDto>> inviteUser(
            @RequestBody LoungeInviteDto request
    ) {
        InviteStatus inviteStatus = loungeFacadeService.inviteUser(request);
        return ResponseEntity.status(inviteStatus.getHttpStatus())
                .body(new ApiResponse<>(inviteStatus.getMessage(), null));
    }
}
