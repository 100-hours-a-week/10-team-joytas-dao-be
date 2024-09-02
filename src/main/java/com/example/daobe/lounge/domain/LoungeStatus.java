package com.example.daobe.lounge.domain;

public enum LoungeStatus {
    ACTIVE,     // 사용 중인 상태
    DELETED,    // 삭제된 상태
    INACTIVE;   // 비활성화된 상태 -> 변경 가능

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isDeleted() {
        return this == DELETED;
    }

    public boolean isInactive() {
        return this == INACTIVE;
    }
}
