package com.example.daobe.user.domain;

import com.example.daobe.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

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
    private String reasons;

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

    public void updateUserInfo(String nickname, String profileUrl) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (profileUrl != null) {
            this.profileUrl = profileUrl;
        }
        if (status.isFirstLogin()) {
            status = UserStatus.ACTIVE;
        }
    }

    public boolean isDeletedUser() {
        return status == UserStatus.DELETED;
    }

    public void updateActiveStatus() {
        this.status = UserStatus.ACTIVE;
    }

    public void withdrawWithAddReason(List<String> stringReasonTypeList, String detail) {
        this.reasons = stringReasonTypeList.stream()
                .map(ReasonType::getReasonTypeByString)
                .toList()
                .toString();
        this.reasonDetail = detail;
        this.status = UserStatus.DELETED;
        this.updateDeletedTime();
    }
}
