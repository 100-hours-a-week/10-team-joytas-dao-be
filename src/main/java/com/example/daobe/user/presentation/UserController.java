package com.example.daobe.user.presentation;

import com.example.daobe.common.throttling.annotation.RateLimited;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.application.dto.PagedUserInfoResponseDto;
import com.example.daobe.user.application.dto.UpdateProfileRequestDto;
import com.example.daobe.user.application.dto.UpdateProfileResponseDto;
import com.example.daobe.user.application.dto.UserInfoResponseDto;
import com.example.daobe.user.application.dto.UserInquiriesRequestDto;
import com.example.daobe.user.application.dto.UserPokeRequestDto;
import com.example.daobe.user.application.dto.UserWithdrawRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponseDto> getCurrentUser(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(userService.getUserInfoWithId(userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userService.getUserInfoWithId(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<PagedUserInfoResponseDto> searchUser(
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "nickname", required = false, defaultValue = "") String nickname
    ) {
        return ResponseEntity.ok(userService.searchUserByNickname(nickname, cursor));
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validateNickname(
            @RequestParam("nickname") String nickname
    ) {
        userService.checkValidateByNickname(nickname);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdrawUser(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserWithdrawRequestDto request
    ) {
        userService.withdraw(userId, request);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/profile")
    public ResponseEntity<UpdateProfileResponseDto> updateProfile(
            @AuthenticationPrincipal Long userId,
            @RequestBody UpdateProfileRequestDto request
    ) {
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    @RateLimited(name = "userPoke", capacity = 3, refillSeconds = 3)
    @PostMapping("/poke")
    public ResponseEntity<Void> poke(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserPokeRequestDto request
    ) {
        userService.poke(userId, request);
        return ResponseEntity.ok(null);
    }

    @RateLimited(name = "userInquiries", capacity = 1, refillSeconds = 30)
    @PostMapping("/inquiries")
    public ResponseEntity<Void> inquiries(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserInquiriesRequestDto request
    ) {
        userService.inquiries(userId, request);
        return ResponseEntity.ok(null);
    }
}
