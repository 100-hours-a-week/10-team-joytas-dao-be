package com.example.daobe.notification.application.dto;

import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.convert.dto.DomainInfo;
import com.example.daobe.user.domain.User;

public record NotificationInfoResponseDto(
        Long notificationId,
        String type,
        boolean isRead,
        UserInfo author,
        DomainInfo detail
) {

    public static NotificationInfoResponseDto of(Notification notification, DomainInfo domainInfo) {
        return new NotificationInfoResponseDto(
                notification.getId(),
                notification.getType().type(),
                notification.isRead(),
                UserInfo.of(notification.getUser()),
                domainInfo
        );
    }

    public record UserInfo(
            Long userId,
            String nickname
    ) {

        public static UserInfo of(User user) {
            return new UserInfo(
                    user.getId(),
                    user.getNickname()
            );
        }
    }
}
