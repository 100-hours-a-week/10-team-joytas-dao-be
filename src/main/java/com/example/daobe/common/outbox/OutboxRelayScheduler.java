package com.example.daobe.common.outbox;

import com.example.daobe.common.domain.DomainEvent;
import com.example.daobe.common.domain.DomainEventConverter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelayScheduler {

    private final OutboxRepository outboxRepository;
    private final DomainEventConverter domainEventConverter;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedRate = 5000)
    @Transactional(readOnly = true)
    @SchedulerLock(name = "outboxRelaySchedulerLock", lockAtLeastFor = "PT5S", lockAtMostFor = "PT10S")
    public void scheduledOutboxRelay() {
        List<Outbox> outboxList = outboxRepository.findAllByIsCompleteFalse();
        for (Outbox outbox : outboxList) {
            DomainEvent event = domainEventConverter.convert(
                    outbox.getAggregateType(), outbox.getPayload()
            );
            eventPublisher.publishEvent(event);
        }
    }
}
