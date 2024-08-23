package com.example.daobe.notification.application;

import com.example.daobe.common.domain.DomainEvent;
import com.example.daobe.lounge.domain.event.LoungeInviteEvent;
import com.example.daobe.notification.application.dto.NotificationPayload;
import com.example.daobe.notification.domain.NotificationEmitter;
import com.example.daobe.notification.domain.NotificationEventType;
import com.example.daobe.notification.domain.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class NotificationPublisher {

    private final EmitterRepository emitterRepository;

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            classes = {
                    LoungeInviteEvent.class
            }
    )
    public void publish(DomainEvent domainEvent) {
        String receiveEmitterId = domainEvent.getReceiveUserId().toString();
        NotificationEmitter emitters = emitterRepository.findAllEmitterByUserId(receiveEmitterId);
        String eventType = NotificationEventType.getEventTypeByDomainEvent(domainEvent);
        emitters.sendToClient(NotificationPayload.of(domainEvent, eventType));
    }
}
