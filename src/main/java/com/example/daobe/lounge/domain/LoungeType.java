package com.example.daobe.lounge.domain;

import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_TYPE_EXCEPTION;

import com.example.daobe.lounge.exception.LoungeException;
import java.util.Arrays;

public enum LoungeType {

    L0001("L0001"),
    L0002("L0002"),
    L0003("L0003"),
    ;

    private final String typeName;

    LoungeType(String typeName) {
        this.typeName = typeName;
    }

    public static LoungeType from(String type) {
        return Arrays.stream(values())
                .filter(loungeType -> loungeType.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new LoungeException(INVALID_LOUNGE_TYPE_EXCEPTION));
    }

    public String getTypeName() {
        return typeName;
    }
}
