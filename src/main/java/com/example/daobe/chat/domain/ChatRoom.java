package com.example.daobe.chat.domain;

import com.example.daobe.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;


@Getter
@Entity
@Table(name = "chat_rooms")
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "room_token", nullable = false, unique = true)
    private String roomToken;

    public ChatRoom() {
        this.roomToken = UUID.randomUUID().toString();
    }
}
