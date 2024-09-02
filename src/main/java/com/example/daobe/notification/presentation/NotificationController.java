package com.example.daobe.notification.presentation;

import com.example.daobe.common.response.ApiResponse;
import com.example.daobe.notification.application.NotificationService;
import com.example.daobe.notification.application.dto.NotificationInfoResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // TODO: 페이징 처리
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationInfoResponseDto>>> getNotificationList(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "NOTIFICATION_LIST_LOADED_SUCCESS",
                notificationService.getNotificationList(userId)
        ));
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
