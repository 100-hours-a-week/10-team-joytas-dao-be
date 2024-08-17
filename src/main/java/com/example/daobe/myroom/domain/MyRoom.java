package com.example.daobe.myroom.domain;

import com.example.daobe.common.entity.BaseTimeEntity;
import com.example.daobe.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "myrooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyRoom extends BaseTimeEntity {

    @Id
    @Column(name = "myroom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    // 마이룸 별명
    private String name;

    // TODO: ENUM 으로 수정 예정
    // 마이룸 타임 ex) R0001, R0002
    private String type;

    @Builder
    public MyRoom(User user, String name, String type) {
        this.user = user;
        this.name = name;
        this.type = type;
    }
}
