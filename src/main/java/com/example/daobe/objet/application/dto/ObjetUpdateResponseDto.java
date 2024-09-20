package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;

public record ObjetUpdateResponseDto(
        Long objetId
) {

    public static ObjetUpdateResponseDto of(Objet objet) {
        return new ObjetUpdateResponseDto(objet.getId());
    }
}
