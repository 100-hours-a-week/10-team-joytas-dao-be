package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;

public record ObjetInfoDto(
        Long objetId,
        ObjetType objetType,
        String name
) {
    public static ObjetInfoDto of(Objet objet) {
        return new ObjetInfoDto(
                objet.getId(),
                objet.getType(),
                objet.getName()
        );
    }

}
