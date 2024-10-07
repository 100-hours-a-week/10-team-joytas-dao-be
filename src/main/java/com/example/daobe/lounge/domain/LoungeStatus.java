package com.example.daobe.lounge.domain;

public enum LoungeStatus {
    ACTIVE,     // 사용 중인 상태
    DELETED,    // 삭제된 상태
    ;

    public boolean isActive() {
        return this == ACTIVE;
    }
}
