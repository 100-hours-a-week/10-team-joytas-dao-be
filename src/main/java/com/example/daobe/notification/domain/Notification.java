package com.example.daobe.notification.domain;

import static com.example.daobe.notification.exception.NotificationExceptionType.IS_NOT_OWN_NOTIFICATION;

import com.example.daobe.common.domain.BaseTimeEntity;
import com.example.daobe.notification.exception.NotificationException;
import com.example.daobe.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @Column(name = "noti_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "receive_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User receiveUser;

    @JoinColumn(name = "send_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User sendUser;

    @Column(name = "domain_id")
    private Long domainId;

    @Enumerated(value = EnumType.STRING)
    private NotificationEventType type;

    @Column(name = "is_read")
    private boolean isRead;

    @Builder
    public Notification(User receiveUser, User sendUser, Long domainId, NotificationEventType type) {
        this.receiveUser = receiveUser;
        this.sendUser = sendUser;
        this.domainId = domainId;
        this.type = type;
        this.isRead = false;
    }

    public void updateReadStateIfOwnNotification(Long userId) {
        if (Objects.equals(receiveUser.getId(), userId)) {
            isRead = true;
        }
        throw new NotificationException(IS_NOT_OWN_NOTIFICATION);
    }
}
