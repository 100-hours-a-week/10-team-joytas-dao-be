package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;
import lombok.Builder;

@Builder
public record ObjetInfoDto(
        Long objetId,
        ObjetType type,
        String name
) {
    public static ObjetInfoDto of(Objet objet) {
        return ObjetInfoDto.builder()
                .objetId(objet.getObjetId())
                .type(objet.getType())
                .name(objet.getName())
                .build();
    }

}
