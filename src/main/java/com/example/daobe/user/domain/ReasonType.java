package com.example.daobe.user.domain;

import java.util.Optional;

public enum ReasonType {
    W0001("서비스에 대한 흥미를 잃었어요"),
    W0002("사용 빈도가 낮아요"),
    W0003("이용이 불편하고 장애가 많아요"),
    W0004("특별한 이유가 없어요"),
    W0005("기타"),
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
