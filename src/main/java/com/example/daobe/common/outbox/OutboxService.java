package com.example.daobe.common.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxService {

    private static final String NOT_EXIST_EVENT_ID_LOG = "존재하지 않는 이벤트입니다.";

    private final OutboxRepository outboxRepository;

    public void completeEvent(String eventId) {
        Outbox outbox = outboxRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException(NOT_EXIST_EVENT_ID_LOG));
        outbox.complete();
        outboxRepository.save(outbox);
    }
}
