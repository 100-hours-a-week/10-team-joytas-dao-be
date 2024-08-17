package com.example.daobe.lounge.domain;

import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.lounge.exception.LoungeExceptionType;
import com.example.daobe.objet.domain.Objet;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "lounges")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lounge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lounge_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    private LoungeType type;

    @Enumerated(EnumType.STRING)
    private LoungeStatus status;

    @Column(name = "reason")
    private String reason;

    @Column(columnDefinition = "TEXT", name = "reason_detail")
    private String reasonDetail;

    @OneToMany(mappedBy = "lounge")
    private List<Objet> objets;

    @OneToMany(mappedBy = "lounge")
    private List<LoungeSharer> loungeSharers;

    @Builder
    public Lounge(User user, String name, LoungeType type, LoungeStatus status) {
        this.user = user;
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public void softDelete(User owner) {
        validateLoungeOwner(owner);
        validateLoungeStatus();
        this.status = LoungeStatus.DELETED;
    }

    public void validateLoungeStatus() {
        if (status.isDeleted() || status.isInactive()) {
            throw new LoungeException(LoungeExceptionType.NOT_ACTIVE_LOUNGE_EXCEPTION);
        }
    }

    private void validateLoungeOwner(User owner) {
        if (!Objects.equals(owner, user)) {
            throw new LoungeException(LoungeExceptionType.INVALID_LOUNGE_OWNER_EXCEPTION);
        }
    }
}
