package com.example.daobe.chat.domain;

import com.example.daobe.common.domain.BaseTimeEntity;
import com.example.daobe.objet.domain.Objet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_rooms")
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "objet_id")
    private Objet objet;

    @Column(name = "room_token", nullable = false, unique = true)
    private String roomToken;

    public ChatRoom(Objet objet) {
        this.objet = objet;
        this.roomToken = UUID.randomUUID().toString();
    }
}
