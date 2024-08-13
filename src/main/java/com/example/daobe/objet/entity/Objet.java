package com.example.daobe.objet.entity;

import com.example.daobe.common.entity.BaseTimeEntity;
import com.example.daobe.lounge.entity.Lounge;
import com.example.daobe.objet.dto.ObjetCreateResponseDto;
import com.example.daobe.shared.entity.UserObjet;
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

@Entity
@Getter
@Table(name = "objets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Objet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objet_id")
    private Long objetId;

    @JoinColumn(name = "lounge_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lounge lounge;

    @OneToMany(mappedBy = "objet")
    private List<UserObjet> userObjets;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "explanation")
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ObjetType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ObjectStatus status;

    @Column(name = "reason")
    private String reason;

    @Column(columnDefinition = "TEXT", name = "reason_detail")
    private String reasonDetail;

    @Builder
    public Objet(Long objetId, Lounge lounge, List<UserObjet> userObjets, String name, String imageUrl,
                 String explanation, ObjetType type, ObjectStatus status, String reason, String reasonDetail) {
        this.objetId = objetId;
        this.lounge = lounge;
        this.userObjets = userObjets;
        this.name = name;
        this.imageUrl = imageUrl;
        this.explanation = explanation;
        this.type = type;
        this.status = status;
        this.reason = reason;
        this.reasonDetail = reasonDetail;
    }

    public ObjetCreateResponseDto toObjetCreateResponseDto() {
        return new ObjetCreateResponseDto(objetId);
    }
}
