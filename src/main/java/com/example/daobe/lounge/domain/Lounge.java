package com.example.daobe.lounge.domain;

import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_OWNER_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.NOT_ACTIVE_LOUNGE_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.NOT_ALLOW_LOUNGE_WITHDRAW_EXCEPTION;

import com.example.daobe.common.domain.BaseTimeEntity;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "lounges")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lounge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lounge_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Embedded
    private LoungeName name;

    @Enumerated(EnumType.STRING)
    private LoungeType type;

    @Enumerated(EnumType.STRING)
    private LoungeStatus status;

    @Column(name = "reason")
    private String reason;

    @Column(columnDefinition = "TEXT", name = "reason_detail")
    private String reasonDetail;

    @Builder
    public Lounge(User user, String name, String type) {
        this.user = user;
        this.name = new LoungeName(name);
        this.type = LoungeType.from(type);
        this.status = LoungeStatus.ACTIVE;
    }

    public Lounge(Long id, User user, String name, String type, LoungeStatus status) {
        this.id = id;
        this.user = user;
        this.name = new LoungeName(name);
        this.type = LoungeType.from(type);
        this.status = status;
    }

    public void softDelete(Long userId) {
        isActiveOrThrow();
        isOwnerOrThrow(userId);
        this.status = LoungeStatus.DELETED;
    }

    public boolean isActive() {
        return status.isActive();
    }

    public void isActiveOrThrow() {
        if (!status.isActive()) {
            throw new LoungeException(NOT_ACTIVE_LOUNGE_EXCEPTION);
        }
    }

    public void isPossibleToWithdrawOrThrow(Long userId) {
        isActiveOrThrow();
        if (userId == user.getId()) {
            throw new LoungeException(NOT_ALLOW_LOUNGE_WITHDRAW_EXCEPTION);
        }
    }

    public String getName() {
        return name.getValue();
    }

    private void isOwnerOrThrow(Long userId) {
        if (userId != user.getId()) {
            throw new LoungeException(INVALID_LOUNGE_OWNER_EXCEPTION);
        }
    }
}
