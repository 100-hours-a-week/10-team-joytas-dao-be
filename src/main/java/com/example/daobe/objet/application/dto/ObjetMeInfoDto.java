package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;
import lombok.Builder;

@Builder
public record ObjetMeInfoDto(
        Long objetId,
        String name,
        String objetImage,
        String description,
        ObjetType objetType,
        Long loungeId
) {
    public static ObjetMeInfoDto of(Objet objet) {
        return ObjetMeInfoDto.builder()
                .objetId(objet.getObjetId())
                .name(objet.getName())
                .objetImage(objet.getImageUrl())
                .description(objet.getExplanation())
                .objetType(objet.getType())
                .loungeId(objet.getLounge().getId())
                .build();
    }
}
