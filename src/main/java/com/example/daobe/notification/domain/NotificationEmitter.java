package com.example.daobe.notification.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class NotificationEmitter {

    private static final String NOTIFICATION_EVENT_NAME = "NOTIFICATION_EVENT";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, SseEmitter> emitterMap;

    public NotificationEmitter(Map<String, SseEmitter> emitterMap) {
        this.emitterMap = emitterMap;
    }

    public void sendToClient(Object event) {
        emitterMap.forEach((key, emitter) -> {
            sendToClient(emitter, generateEmitterId(), event);
        });
    }

    public SseEmitter getSingleEmitter() {
        if (emitterMap.size() == 1) {
            return emitterMap.values().stream()
                    .findFirst()
                    .get();
        }
        throw new RuntimeException("생성된 이미터가 1개가 아닙니다");
    }

    private String generateEmitterId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private void sendToClient(
            SseEmitter emitter,
            String emitterId,
            Object data
    ) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .id(emitterId)
                            .name(NOTIFICATION_EVENT_NAME)
                            .data(extractEventDataAsJson(data))
            );
        } catch (IOException | RuntimeException ex) {
            emitter.complete();
        }
    }

    private String extractEventDataAsJson(Object data) {
        final String jsonValue;
        try {
            jsonValue = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        return jsonValue;
    }
}
