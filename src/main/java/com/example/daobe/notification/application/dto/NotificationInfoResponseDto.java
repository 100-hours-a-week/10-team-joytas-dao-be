package com.example.daobe.notification.application.dto;

import com.example.daobe.notification.domain.Notification;
import com.example.daobe.notification.domain.convert.dto.DomainInfo;
import com.example.daobe.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NotificationInfoResponseDto(
        @JsonProperty("notification_id") Long notificationId,
        @JsonProperty("type") String type,
        @JsonProperty("is_read") boolean isRead,
        @JsonProperty("datetime") String dateTime,
        @JsonProperty("sender") UserInfo sender,
        @JsonProperty("detail") DomainInfo detail
) {

    public static NotificationInfoResponseDto of(Notification notification, DomainInfo domainInfo) {
        return new NotificationInfoResponseDto(
                notification.getId(),
                notification.getType().type(),
                notification.isRead(),
                notification.getCreatedAt().toString(),
                UserInfo.of(notification.getSendUser()),
                domainInfo
        );
    }

    // Nested
    private record UserInfo(
            @JsonProperty("user_id") Long userId,
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
