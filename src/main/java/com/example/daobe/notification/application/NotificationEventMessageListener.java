package com.example.daobe.notification.application;

import com.example.daobe.common.domain.DomainEvent;
import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.NotificationEventType;
import com.example.daobe.notification.domain.repository.NotificationRepository;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class NotificationDomainEventListener {

    private final UserService userService;
    private final NotificationRepository notificationRepository;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void listenDomainEvent(DomainEvent domainEvent) {
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
        notificationRepository.save(newNotification);
    }
}
