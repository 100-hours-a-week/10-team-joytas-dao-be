package com.example.daobe.user.entity;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("활성화된 계정"),
    ACTIVE_FIRST_LOGIN("최초 로그인된 계정"),  // 최초 로그인 상태
    DELETED("삭제된 계정"),
    INACTIVE("비활성 계정"),
    ;

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public boolean isFirstLogin() {
        return this == UserStatus.ACTIVE_FIRST_LOGIN;
    }
}
