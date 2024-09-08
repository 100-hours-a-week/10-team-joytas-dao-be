package com.example.daobe.notification.application;

import com.example.daobe.notification.application.dto.NotificationEventPayloadDto;
import com.example.daobe.notification.application.dto.NotificationInfoResponseDto;
import com.example.daobe.notification.domain.NotificationEmitter;
import com.example.daobe.notification.domain.repository.EmitterRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventListener {

    private final EmitterRepository emitterRepository;

    public void publishToClient(NotificationEventPayloadDto payload) {
        List<NotificationEmitter> emitterList = emitterRepository.findAllByUserId(payload.receiveUserId());
        emitterList.forEach(emitter -> sendToClient(emitter, payload.data()));
    }

    private void sendToClient(NotificationEmitter emitter, NotificationInfoResponseDto data) {
        try {
            emitter.sendToClient(data);
        } catch (Exception ex) {
            emitterRepository.deleteById(emitter.getEmitterId());
        }
    }
}
