package com.example.daobe.notification.domain.repository;

import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.repository.dto.NotificationFindCondition;
import org.springframework.data.domain.Slice;

public interface CustomNotificationRepository {

    Slice<Notification> findNotificationByCondition(NotificationFindCondition condition);
}
