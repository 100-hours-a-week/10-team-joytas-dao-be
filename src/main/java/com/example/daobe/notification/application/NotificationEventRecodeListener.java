package com.example.daobe.notification.application;

import com.example.daobe.common.domain.DomainEvent;
import com.example.daobe.common.outbox.Outbox;
import com.example.daobe.common.outbox.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class NotificationEventRecodeListener {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final OutboxRepository outboxRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void eventRecodeListener(DomainEvent domainEvent) {
        String payload = serializeToString(domainEvent);
        Outbox outbox = Outbox.builder()
                .id(domainEvent.getEventId())
                .aggregateType(domainEvent.getClass().getSimpleName())
                .payload(payload)
                .build();
        outboxRepository.save(outbox);
    }

    private String serializeToString(DomainEvent domainEvent) {
        try {
            return objectMapper.writeValueAsString(domainEvent);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
