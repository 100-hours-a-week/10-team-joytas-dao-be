package com.example.daobe.lounge.presentation;

import com.example.daobe.lounge.application.LoungeFacadeService;
import com.example.daobe.lounge.application.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.application.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.application.dto.LoungeDetailResponseDto;
import com.example.daobe.lounge.application.dto.LoungeInfoResponseDto;
import com.example.daobe.lounge.application.dto.LoungeInviteRequestDto;
import com.example.daobe.lounge.application.dto.LoungeSharerInfoResponseDto;
import com.example.daobe.lounge.application.dto.LoungeValidateRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<LoungeCreateResponseDto> createLounge(
            @AuthenticationPrincipal Long userId,
            @RequestBody LoungeCreateRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loungeFacadeService.createLounge(request, userId));
    }

    @GetMapping
    public ResponseEntity<List<LoungeInfoResponseDto>> getAllLounges(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(loungeFacadeService.getAllLounges(userId));
    }

    @GetMapping("/{loungeId}")
    public ResponseEntity<LoungeDetailResponseDto> getLoungeDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "loungeId") Long loungeId
    ) {
        return ResponseEntity.ok(loungeFacadeService.getLoungeDetail(userId, loungeId));
    }

    @DeleteMapping("/{loungeId}")
    public ResponseEntity<Void> deleteLounge(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long loungeId
    ) {
        loungeFacadeService.deleteLounge(userId, loungeId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/invite")
    public ResponseEntity<Void> inviteUser(
            @AuthenticationPrincipal Long userId,
            @RequestBody LoungeInviteRequestDto request
    ) {
        loungeFacadeService.inviteUser(request, userId);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/{loungeId}/invite/accept")
    public ResponseEntity<Void> updateInvitedUserStatus(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long loungeId
    ) {
        loungeFacadeService.updateInvitedUserStatus(userId, loungeId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{loungeId}/search")
    public ResponseEntity<List<LoungeSharerInfoResponseDto>> searchLoungeSharer(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long loungeId,
            @RequestParam(value = "nickname", defaultValue = "") String nickname
    ) {
        return ResponseEntity.ok(loungeFacadeService.searchLoungeSharer(userId, nickname, loungeId));
    }

    @PostMapping("/{loungeId}/withdraw")
    public ResponseEntity<Void> withDraw(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long loungeId
    ) {
        loungeFacadeService.withdraw(userId, loungeId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validate(
            @AuthenticationPrincipal Long userId,
            @RequestBody LoungeValidateRequestDto request
    ) {
        loungeFacadeService.isLoungeSharer(userId, request);
        return ResponseEntity.ok(null);
    }
}
