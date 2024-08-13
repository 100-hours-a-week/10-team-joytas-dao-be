package com.example.daobe.lounge.entity;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum LoungeType {
    L0001("l0001"),
    L0002("l0002"),
    L0003("l0003");

    private final String type;

    LoungeType(String type) {
        this.type = type;
    }

    public static LoungeType from(String type) {
        return Arrays.stream(values())
                .filter(loungeType -> loungeType.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("NOT_EXISTS_LOUNGE_TYPE_EXCEPTION"));
    }
}
