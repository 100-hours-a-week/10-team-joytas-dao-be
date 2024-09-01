package com.example.daobe.notification.domain.repository;

import com.example.daobe.notification.domain.NotificationEmitter;
import java.util.List;

public interface EmitterRepository {

    void save(NotificationEmitter notificationEmitter);

    void deleteById(String emitterId);

    List<NotificationEmitter> findAllByUserId(Long receiveUserId);
}
