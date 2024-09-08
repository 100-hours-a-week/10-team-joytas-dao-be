package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;

public record ObjetInfoDto(
        Long objetId,
        ObjetType type,
        String name
) {
    public static ObjetInfoDto of(Objet objet) {
        return new ObjetInfoDto(
                objet.getObjetId(),
                objet.getType(),
                objet.getName()
        );
    }

}
