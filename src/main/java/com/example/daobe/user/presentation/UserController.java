package com.example.daobe.user.presentation;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.application.dto.UpdateProfileRequestDto;
import com.example.daobe.user.application.dto.UpdateProfileResponseDto;
import com.example.daobe.user.application.dto.UserInfoResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<ApiResponse<List<UserInfoResponseDto>>> update(
            @RequestParam("nickname") String nickname
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "USER_LIST_LOADED_SUCCESS",
                userService.searchUserByNickname(nickname)
        ));
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

    @PatchMapping
    public ResponseEntity<ApiResponse<UpdateProfileResponseDto>> updateProfile(
            @AuthenticationPrincipal Long userId,
            @RequestBody UpdateProfileRequestDto request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "UPDATE_PROFILE_SUCCESS",
                userService.updateNickname(userId, request)
        ));
    }
}
