package com.example.daobe.user.domain;

import java.util.Optional;

public enum ReasonType {
    W0001(""),
    W0002(""),
    W0003(""),
    W0004(""),
    W0005(""),
    ;

    private final String description;

    ReasonType(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    public static ReasonType getReasonTypeByString(String stringValue) {
        return Optional.of(ReasonType.valueOf(stringValue.toUpperCase()))
                .orElseThrow(() -> new RuntimeException("일치하는 타입이 없습니다."));
    }
}
