package com.example.daobe.notification.application;

import com.example.daobe.notification.application.dto.DummyResponseDto;
import com.example.daobe.notification.domain.NotificationEmitter;
import com.example.daobe.notification.domain.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationSubscribeService {

    private static final Long DEFAULT_TIMEOUT = 44 * 1000L;  // 기본 지속 시간 44초

    private final EmitterRepository emitterRepository;

    public SseEmitter subscribeNotification(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        NotificationEmitter notificationEmitter = NotificationEmitter.builder()
                .userId(userId)
                .emitter(emitter)
                .build();
        emitterRepository.save(notificationEmitter);

        configurationEmitter(emitter, notificationEmitter.getEmitterId());
        sendDummyNotification(notificationEmitter);

        return emitter;
    }

    private void configurationEmitter(SseEmitter emitter, String emitterId) {
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> {
            emitter.complete();
            emitterRepository.deleteById(emitterId);
        });
        emitter.onError((error) -> {
            emitter.complete();
            emitterRepository.deleteById(emitterId);
        });
    }

    private void sendDummyNotification(NotificationEmitter emitter) {
        try {
            emitter.sendToClient(DummyResponseDto.of());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
