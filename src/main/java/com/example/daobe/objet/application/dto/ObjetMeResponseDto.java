package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;

public record ObjetMeResponseDto(
        Long objetId,
        String name,
        String objetImage,
        String description,
        ObjetType objetType,
        Long loungeId
) {
    public static ObjetMeResponseDto of(Objet objet) {
        return new ObjetMeResponseDto(
                objet.getId(),
                objet.getName(),
                objet.getImageUrl(),
                objet.getExplanation(),
                objet.getType(),
                objet.getLounge().getId()
        );
    }
}
