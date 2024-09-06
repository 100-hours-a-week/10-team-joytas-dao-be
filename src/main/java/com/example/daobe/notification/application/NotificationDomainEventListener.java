package com.example.daobe.notification.application;

import static com.example.daobe.user.exception.UserExceptionType.NOT_EXIST_USER;

import com.example.daobe.common.domain.DomainEvent;
import com.example.daobe.notification.application.dto.NotificationInfoResponseDto;
import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.NotificationEmitter;
import com.example.daobe.notification.domain.NotificationEventType;
import com.example.daobe.notification.domain.convert.DomainEventConvertMapper;
import com.example.daobe.notification.domain.convert.dto.DomainInfo;
import com.example.daobe.notification.domain.repository.EmitterRepository;
import com.example.daobe.notification.domain.repository.NotificationRepository;
import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.repository.UserRepository;
import com.example.daobe.user.exception.UserException;
import java.util.List;
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
    private final DomainEventConvertMapper domainEventConvertMapper;

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            classes = DomainEvent.class
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishEvent(DomainEvent domainEvent) {
        Long sendUserId = domainEvent.getSendUserId();
        User sendUser = userRepository.findById(sendUserId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));

        Long receiveUserId = domainEvent.getReceiveUserId();
        User receiveUser = userRepository.findById(receiveUserId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));

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

        List<NotificationEmitter> emitterList = emitterRepository.findAllByUserId(receiveUserId);
        emitterList.forEach((emitter) -> emitter.sendToClient(payload));
    }
}
