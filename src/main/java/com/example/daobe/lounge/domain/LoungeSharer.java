package com.example.daobe.lounge.domain;

import static com.example.daobe.lounge.domain.LoungeSharerStatus.ACTIVE;
import static com.example.daobe.lounge.domain.LoungeSharerStatus.PENDING;

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
@Table(name = "lounges_sharers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoungeSharer {

    @Id
    @Column(name = "lng_shr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "lounge_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lounge lounge;

    @Enumerated(EnumType.STRING)
    private LoungeSharerStatus status;

    @Builder
    public LoungeSharer(User user, Lounge lounge, LoungeSharerStatus status) {
        this.user = user;
        this.lounge = lounge;
        this.status = Objects.requireNonNullElse(status, PENDING);
    }

    public LoungeSharer(Long id, User user, Lounge lounge, LoungeSharerStatus status) {
        this.id = id;
        this.user = user;
        this.lounge = lounge;
        this.status = status;
    }

    public boolean isActive() {
        return status.isActive();
    }

    public void updateStatusActive() {
        this.status = ACTIVE;
    }
}
