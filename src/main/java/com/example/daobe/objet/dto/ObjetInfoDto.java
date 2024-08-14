package com.example.daobe.objet.dto;

import com.example.daobe.objet.entity.Objet;
import com.example.daobe.objet.entity.ObjetType;
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
