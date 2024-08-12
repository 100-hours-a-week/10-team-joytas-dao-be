package com.example.daobe.user.entity;

import com.example.daobe.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
    private Long kakaoId;

    private String nickname;

    @Column(name = "profile_url")
    private String profileUrl;

    private String status;

    // 비활성화 사유
    private String reason;

    // 비활성화 사유
    @Column(columnDefinition = "TEXT", name = "reason_detail")
    private String reasonDetail;
}
