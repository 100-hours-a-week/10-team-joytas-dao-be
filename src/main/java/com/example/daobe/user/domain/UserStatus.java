package com.example.daobe.user.domain;

public enum UserStatus {
    ACTIVE("활성화된 계정"),
    ACTIVE_FIRST_LOGIN("최초 로그인된 계정"),
    DELETED("삭제된 계정"),
    INACTIVE("비활성 계정"),
    ;

    private final String description;

    public String description() {
        return description;
    }

    UserStatus(String description) {
        this.description = description;
    }

    public boolean isFirstLogin() {
        return this == UserStatus.ACTIVE_FIRST_LOGIN;
    }
}
