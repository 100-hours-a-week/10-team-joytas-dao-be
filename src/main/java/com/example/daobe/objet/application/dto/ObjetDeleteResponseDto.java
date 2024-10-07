package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;

public record ObjetDeleteResponseDto(
        Long loungeId
) {

    public static ObjetDeleteResponseDto of(Objet objet) {
        return new ObjetDeleteResponseDto(
                objet.getLounge().getId()
        );
    }
}
