package com.example.daobe.notification.application;

import static com.example.daobe.notification.exception.NotificationExceptionType.NOT_EXIST_NOTIFICATION;

import com.example.daobe.notification.application.dto.NotificationInfoResponseDto;
import com.example.daobe.notification.application.dto.PagedNotificationInfoResponseDto;
import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.convert.DomainEventConvertMapper;
import com.example.daobe.notification.domain.convert.dto.DomainInfo;
import com.example.daobe.notification.domain.repository.CustomNotificationRepository;
import com.example.daobe.notification.domain.repository.NotificationRepository;
import com.example.daobe.notification.domain.repository.dto.NotificationFindCondition;
import com.example.daobe.notification.exception.NotificationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private static final int VIEW_LIMIT_SIZE = 10;
    private static final int EXECUTE_LIMIT_SIZE = VIEW_LIMIT_SIZE + 1;

    private final DomainEventConvertMapper domainEventConvertMapper;
    private final NotificationRepository notificationRepository;
    private final CustomNotificationRepository customNotificationRepository;

    public PagedNotificationInfoResponseDto getNotificationList(Long userId, Long cursor) {
        NotificationFindCondition condition = new NotificationFindCondition(userId, cursor, EXECUTE_LIMIT_SIZE);
        Slice<Notification> notificationSlice =
                customNotificationRepository.findNotificationByCondition(condition);
        List<Notification> notificationList = notificationSlice.getContent();
        List<NotificationInfoResponseDto> notificationInfoList = notificationList.stream()
                .map(this::convertNotificationDomainInfo)
                .toList();
        if (notificationSlice.hasNext()) {
            Long nextCursor = notificationInfoList.get(notificationSlice.getSize() - 1).notificationId();
            return PagedNotificationInfoResponseDto.of(notificationSlice.hasNext(), nextCursor, notificationInfoList);
        }
        return PagedNotificationInfoResponseDto.of(notificationSlice.hasNext(), notificationInfoList);
    }

    private NotificationInfoResponseDto convertNotificationDomainInfo(Notification notification) {
        DomainInfo domainInfo = domainEventConvertMapper.convert(notification.getType(), notification.getDomainId());
        return NotificationInfoResponseDto.of(notification, domainInfo);
    }

    @Transactional
    public void readNotification(Long userId, Long notificationId) {
        Notification findNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(NOT_EXIST_NOTIFICATION));
        findNotification.updateReadStateIfOwnNotification(userId);
        notificationRepository.save(findNotification);
    }
}
