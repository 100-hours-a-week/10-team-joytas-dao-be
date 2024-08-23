package com.example.daobe.notification.presentation;

import com.example.daobe.notification.application.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private static final String NGINX_X_ACCEL_BUFFERING_HEADER = "X-Accel-Buffering";
    private static final String NO = "no";
    private static final String KEEP_ALIVE = "Keep-Alive";
    private static final String TIMEOUT = "timeout=";
    private static final int TIMEOUT_VALUE = 44;
    private static final String CONNECTION = "Connection";
    private static final String CONNECTION_KEEP_ALIVE = "keep-alive";

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connection(
            @AuthenticationPrincipal Long userId
    ) {
        SseEmitter emitter = notificationService.subscribeNotification(userId);
        return ResponseEntity.ok()
                .header(NGINX_X_ACCEL_BUFFERING_HEADER, NO)
                .header(KEEP_ALIVE, TIMEOUT + TIMEOUT_VALUE)
                .header(CONNECTION, CONNECTION_KEEP_ALIVE)
                .body(emitter);
    }
}

