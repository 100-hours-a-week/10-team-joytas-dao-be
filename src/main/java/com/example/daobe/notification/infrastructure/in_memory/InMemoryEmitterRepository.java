package com.example.daobe.notification.infrastructure.in_memory;

import com.example.daobe.notification.domain.NotificationEmitter;
import com.example.daobe.notification.domain.repository.EmitterRepository;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Repository
public class InMemoryEmitterRepository implements EmitterRepository {

    private static final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    @Override
    public NotificationEmitter save(String emitterId, SseEmitter sseEmitter) {
        sseEmitter.onCompletion(() -> remove(emitterId));
        sseEmitter.onTimeout(() -> {
            remove(emitterId);
            sseEmitter.complete();
        });
        sseEmitter.onError((error) -> {
            log.error(error.getMessage());
            remove(emitterId);
            sseEmitter.completeWithError(error);
        });

        emitterMap.put(emitterId, sseEmitter);

        return new NotificationEmitter(Map.of(emitterId, sseEmitter));
    }

    @Override
    public NotificationEmitter findAllEmitterByUserId(String userId) {
        Map<String, SseEmitter> emitter = emitterMap.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        return new NotificationEmitter(emitter);
    }

    private void remove(String emitterId) {
        emitterMap.remove(emitterId);
    }
}
