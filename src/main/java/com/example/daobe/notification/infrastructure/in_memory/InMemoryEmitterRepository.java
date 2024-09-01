package com.example.daobe.notification.infrastructure.in_memory;

import com.example.daobe.notification.domain.NotificationEmitter;
import com.example.daobe.notification.domain.repository.EmitterRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryEmitterRepository implements EmitterRepository {

    private static final Map<Long, Map<String, NotificationEmitter>> emitterMap = new ConcurrentHashMap<>();

    @Override
    public void save(NotificationEmitter notificationEmitter) {
        emitterMap
                .computeIfAbsent(notificationEmitter.getUserId(), userId -> new ConcurrentHashMap<>())
                .put(notificationEmitter.getEmitterId(), notificationEmitter);
    }

    @Override
    public void deleteById(String emitterId) {
        emitterMap.values().forEach(map -> map.remove(emitterId));
    }

    @Override
    public List<NotificationEmitter> findAllByUserId(Long receiveUserId) {
        Map<String, NotificationEmitter> emitters = emitterMap.get(receiveUserId);
        if (emitters == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(emitters.values());
    }
}
