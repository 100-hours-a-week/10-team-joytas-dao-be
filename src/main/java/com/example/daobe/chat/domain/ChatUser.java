package com.example.daobe.chat.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "chat_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatUser {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("user_id")
    private Long userId;

    @Field("nickname")
    private String nickname;

    @Field("profile_url")
    private String profileUrl;

    @Builder
    public ChatUser(Long userId, String nickname, String profileUrl) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }

    public void updateUserInfo(String nickname, String profileUrl) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (profileUrl != null) {
            this.profileUrl = profileUrl;
        }
    }
}
