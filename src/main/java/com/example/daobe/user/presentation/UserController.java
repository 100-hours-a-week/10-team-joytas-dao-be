package com.example.daobe.user.presentation;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.common.response.SliceApiResponse;
import com.example.daobe.common.throttling.annotation.RateLimited;
import com.example.daobe.user.application.UserService;
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
    public ResponseEntity<ApiResponse<UserInfoResponseDto>> getCurrentUser(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "CURRENT_USER_INFO_LOADED_SUCCESS",
                userService.getUserInfoWithId(userId)
        ));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserInfoResponseDto>> getUserInfo(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "LOAD_USER_INFO_SUCCESS",
                userService.getUserInfoWithId(userId)
        ));
    }

    @GetMapping("/search")
    public ResponseEntity<SliceApiResponse<UserInfoResponseDto>> searchUser(
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "nickname", required = false, defaultValue = "") String nickname
    ) {
        return ResponseEntity.ok(userService.searchUserByNickname(nickname, cursor));
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Void>> validateNickname(
            @RequestParam("nickname") String nickname
    ) {
        userService.checkValidateByNickname(nickname);
        return ResponseEntity.ok(new ApiResponse<>(
                "NICKNAME_DUPLICATE_CHECK_SUCCESS",
                null
        ));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdrawUser(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserWithdrawRequestDto request
    ) {
        userService.withdraw(userId, request);
        return ResponseEntity.ok(new ApiResponse<>(
                "WITHDRAW_SUCCESS",
                null
        ));
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UpdateProfileResponseDto>> updateProfile(
            @AuthenticationPrincipal Long userId,
            @RequestBody UpdateProfileRequestDto request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "UPDATE_PROFILE_SUCCESS",
                userService.updateProfile(userId, request)
        ));
    }

    @RateLimited(name = "userPoke", capacity = 3, refillSeconds = 3)
    @PostMapping("/poke")
    public ResponseEntity<ApiResponse<Void>> poke(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserPokeRequestDto request
    ) {
        userService.poke(userId, request);
        return ResponseEntity.ok(new ApiResponse<>(
                "USER_POKE_SUCCESS",
                null
        ));
    }

    @RateLimited(name = "userInquiries", capacity = 1, refillSeconds = 30)
    @PostMapping("/inquiries")
    public ResponseEntity<ApiResponse<Void>> inquiries(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserInquiriesRequestDto request
    ) {
        userService.inquiries(userId, request);
        return ResponseEntity.ok(new ApiResponse<>(
                "INQUIRIES_SUCCESS",
                null
        ));
    }
}
