package com.example.daobe.notification.domain.repository;

import com.example.daobe.notification.domain.NotificationEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

    NotificationEmitter save(String emitterId, SseEmitter emitter);

    NotificationEmitter findAllEmitterByUserId(String emitterId);
}
