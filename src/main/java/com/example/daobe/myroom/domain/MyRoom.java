package com.example.daobe.myroom.domain;

import static com.example.daobe.myroom.exception.MyRoomExceptionType.FORBIDDEN_MY_ROOM_MODIFICATION;

import com.example.daobe.common.domain.BaseTimeEntity;
import com.example.daobe.myroom.exception.MyRoomException;
import com.example.daobe.user.domain.User;
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
@Table(name = "my_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyRoom extends BaseTimeEntity {

    @Id
    @Column(name = "my_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    // 마이룸 별명
    private String name;

    // 마이룸 타임 ex) R0001, R0002
    private String type;

    @Builder
    public MyRoom(User user, String type) {
        generateDefaultRoomName(user);
        this.user = user;
        this.type = type;
    }

    public void updatedName(String newName) {
        this.name = newName;
    }

    public void isMatchOwnerOrThrow(Long userId) {
        if (!user.getId().equals(userId)) {
            throw new MyRoomException(FORBIDDEN_MY_ROOM_MODIFICATION);
        }
    }

    private void generateDefaultRoomName(User user) {
        this.name = user.getNickname() + "님의 마이룸";
    }
}
