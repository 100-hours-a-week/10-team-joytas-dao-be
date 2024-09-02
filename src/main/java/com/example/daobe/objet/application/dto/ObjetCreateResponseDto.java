package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;

public record ObjetCreateResponseDto(
        Long objetId
) {

    public static ObjetCreateResponseDto of(Objet objet) {
        return new ObjetCreateResponseDto(objet.getObjetId());
    }
}
