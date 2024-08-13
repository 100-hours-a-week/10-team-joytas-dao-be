package com.example.daobe.objet.dto;

import com.example.daobe.objet.entity.Objet;

public record ObjetCreateResponseDto(
        Long objetId
) {

    public static ObjetCreateResponseDto of(Objet objet) {
        return new ObjetCreateResponseDto(objet.getObjetId());
    }
}
