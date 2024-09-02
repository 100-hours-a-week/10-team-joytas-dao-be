package com.example.daobe.lounge.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum LoungeType {

    L0001("L0001"),
    L0002("L0002"),
    L0003("L0003");

    private final String typeName;

    LoungeType(String typeName) {
        this.typeName = typeName;
    }

    public static LoungeType from(String type) {
        return Arrays.stream(values())
                .filter(loungeType -> loungeType.getTypeName().equals(type))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("NOT_EXISTS_LOUNGE_TYPE_EXCEPTION"));
    }
}
