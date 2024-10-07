package com.example.daobe.objet.domain;

public enum ObjetSharerStatus {
    DELETED,
    ACTIVE,
    ;

    public boolean isActive() {
        return this == ACTIVE;
    }
}
