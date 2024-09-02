package com.example.daobe.objet.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ObjetType {
    O0001("O0001"),
    O0002("O0002"),
    O0003("O0003");

    private final String type;

    ObjetType(String type) {
        this.type = type;
    }

    public static ObjetType from(String type) {
        return Arrays.stream(values())
                .filter(ObjetType -> ObjetType.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("NOT_EXISTS_LOUNGE_TYPE_EXCEPTION"));
    }
}
