package com.example.daobe.objet.domain;

import com.example.daobe.objet.exception.ObjetException;
import com.example.daobe.objet.exception.ObjetExceptionType;
import java.util.Arrays;

public enum ObjetType {
    O0001("O0001"),
    O0002("O0002"),
    O0003("O0003"),
    ;

    private final String type;

    ObjetType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ObjetType from(String type) {
        return Arrays.stream(values())
                .filter(ObjetType -> ObjetType.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new ObjetException(ObjetExceptionType.NOT_EXISTS_OBJET_TYPE));
    }
}
