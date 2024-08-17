package com.example.daobe.user.domain;

import com.example.daobe.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    // TODO: VO 필요함

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id")
    private String kakaoId;

    private String nickname;

    @Column(name = "profile_url")
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    // 비활성화 사유
    private String reason;

    // 비활성화 사유
    @Column(columnDefinition = "TEXT", name = "reason_detail")
    private String reasonDetail;

    @Builder
    public User(String kakaoId, String profileUrl) {
        this.kakaoId = kakaoId;
        this.nickname = DefaultNicknamePolicy.generatedRandomString();
        this.profileUrl = profileUrl;
        this.status = UserStatus.ACTIVE_FIRST_LOGIN;
    }

    private void updateNickname(String nickname) {
        this.nickname = nickname;
        changeToActiveStatus();
    }

    private void changeToActiveStatus() {
        if (status.isFirstLogin()) {
            status = UserStatus.ACTIVE;
        }
    }
}
