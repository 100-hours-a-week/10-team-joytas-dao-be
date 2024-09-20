package com.example.daobe.lounge.domain;

public enum LoungeSharerStatus {
    PENDING,
    ACTIVE,
    ;

    public boolean isActive() {
        return this == ACTIVE;
    }
}
