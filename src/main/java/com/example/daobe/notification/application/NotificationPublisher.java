package com.example.daobe.notification.application;

import static com.example.daobe.user.exception.UserExceptionType.NOT_EXIST_USER;

import com.example.daobe.common.domain.DomainEvent;
import com.example.daobe.notification.application.dto.NotificationPayload;
import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.NotificationEmitter;
import com.example.daobe.notification.domain.NotificationEventType;
import com.example.daobe.notification.domain.repository.EmitterRepository;
import com.example.daobe.notification.domain.repository.NotificationRepository;
import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.repository.UserRepository;
import com.example.daobe.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationPublisher {

    private final UserRepository userRepository;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            classes = DomainEvent.class
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishEvent(DomainEvent domainEvent) {
        Long receiveUserId = domainEvent.getReceiveUserId();
        Long sendUserId = domainEvent.getSendUserId();
        String receiveEmitterId = receiveUserId.toString();
        NotificationEmitter emitters = emitterRepository.findAllEmitterByUserId(receiveEmitterId);
        NotificationEventType eventType = NotificationEventType.getEventTypeByDomainEvent(domainEvent);
        emitters.sendToClient(NotificationPayload.of(domainEvent, eventType.type()));

        User sendUser = userRepository.findById(sendUserId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
        User receiveUser = userRepository.findById(receiveUserId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));

        Notification newNotification = Notification.builder()
                .sendUser(sendUser)
                .receiveUser(receiveUser)
                .domainId(domainEvent.getDomainId())
                .type(eventType)
                .build();
        notificationRepository.save(newNotification);
    }
}
