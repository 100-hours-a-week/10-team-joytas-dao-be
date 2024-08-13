package com.example.daobe.lounge.entity;

import com.example.daobe.lounge.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.dto.LoungeInfoDto;
import com.example.daobe.objet.entity.Objet;
import com.example.daobe.shared.entity.UserLounge;
import com.example.daobe.user.entity.User;
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
    private Long loungeId;

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
    private List<UserLounge> userLounges;

    @Builder
    public Lounge(User user, String name, LoungeType type, LoungeStatus status) {
        this.user = user;
        this.name = name;
        this.type = type;
        this.status = status;
    }
}
