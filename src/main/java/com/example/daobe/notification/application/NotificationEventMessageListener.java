package com.example.daobe.notification.application;

import com.example.daobe.common.domain.DomainEvent;
import com.example.daobe.common.outbox.OutboxService;
import com.example.daobe.notification.application.dto.NotificationEventPayloadDto;
import com.example.daobe.notification.application.dto.NotificationInfoResponseDto;
import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.NotificationEventType;
import com.example.daobe.notification.domain.convert.DomainEventConvertMapper;
import com.example.daobe.notification.domain.convert.dto.DomainInfo;
import com.example.daobe.notification.domain.repository.NotificationRepository;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class NotificationEventMessageListener {

    private final UserService userService;
    private final OutboxService outboxService;
    private final NotificationRepository notificationRepository;
    private final DomainEventConvertMapper domainEventConvertMapper;
    private final NotificationExternalEventPublisher notificationExternalEventPublisher;

    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void listenEventMessage(DomainEvent domainEvent) {
        Long sendUserId = domainEvent.getSendUserId();
        User sendUser = userService.getUserById(sendUserId);

        Long receiveUserId = domainEvent.getReceiveUserId();
        User receiveUser = userService.getUserById(receiveUserId);

        NotificationEventType eventType = NotificationEventType.getEventTypeByDomainEvent(domainEvent);

        Notification newNotification = Notification.builder()
                .sendUser(sendUser)
                .receiveUser(receiveUser)
                .domainId(domainEvent.getDomainId())
                .type(eventType)
                .build();
        Notification notification = notificationRepository.save(newNotification);

        DomainInfo domainInfo = domainEventConvertMapper.convert(eventType, domainEvent.getDomainId());
        NotificationInfoResponseDto payload = NotificationInfoResponseDto.of(notification, domainInfo);

        notificationExternalEventPublisher.execute(NotificationEventPayloadDto.of(receiveUserId, payload));

        outboxService.completeEvent(domainEvent.getEventId());
    }
}
