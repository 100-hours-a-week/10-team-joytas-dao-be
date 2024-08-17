package com.example.daobe.objet.domain;

import com.example.daobe.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "objets_sharers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ObjetSharer {

    @Id
    @Column(name = "usr_obj_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "objet_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Objet objet;

    @Builder
    public ObjetSharer(User user, Objet objet) {
        this.user = user;
        this.objet = objet;
    }
}
