package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;

public record ObjetInfoResponseDto(
        Long objetId
) {

    public static ObjetInfoResponseDto of(Objet objet) {
        return new ObjetInfoResponseDto(objet.getObjetId());
    }
}
