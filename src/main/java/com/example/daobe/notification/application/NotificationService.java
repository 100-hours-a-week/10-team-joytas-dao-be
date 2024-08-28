package com.example.daobe.notification.application;

import static com.example.daobe.notification.exception.NotificationExceptionType.NOT_EXIST_NOTIFICATION;

import com.example.daobe.notification.application.dto.NotificationInfoResponseDto;
import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.convert.DomainEventConvertMapper;
import com.example.daobe.notification.domain.convert.dto.DomainInfo;
import com.example.daobe.notification.domain.repository.NotificationRepository;
import com.example.daobe.notification.exception.NotificationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final DomainEventConvertMapper domainEventConvertMapper;
    private final NotificationRepository notificationRepository;

    public List<NotificationInfoResponseDto> getNotificationList(Long userId) {
        List<Notification> notificationList = notificationRepository.findByUserId(userId);

        return notificationList.stream()
                .map((notification) -> {
                    DomainInfo domainInfo = domainEventConvertMapper.convert(
                            notification.getType(), notification.getDomainId()
                    );
                    return NotificationInfoResponseDto.of(notification, domainInfo);
                })
                .toList();
    }

    @Transactional
    public void readNotification(Long userId, Long notificationId) {
        Notification findNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(NOT_EXIST_NOTIFICATION));
        findNotification.updateReadStateIfOwnNotification(userId);
        notificationRepository.save(findNotification);
    }
}
