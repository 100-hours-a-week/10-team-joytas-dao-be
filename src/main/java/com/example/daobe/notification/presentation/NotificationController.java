package com.example.daobe.notification.presentation;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.common.response.SliceApiResponse;
import com.example.daobe.notification.application.NotificationService;
import com.example.daobe.notification.application.dto.NotificationInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<SliceApiResponse<NotificationInfoResponseDto>> getNotificationList(
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "cursor", required = false) Long cursor
    ) {
        return ResponseEntity.ok(notificationService.getNotificationList(userId, cursor));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> readNotification(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long notificationId
    ) {
        notificationService.readNotification(userId, notificationId);
        return ResponseEntity.ok(new ApiResponse<>(
                "NOTIFICATION_READ_SUCCESS",
                null
        ));
    }
}
