package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;
import java.util.List;

public record ObjetResponseDto(
        Long objetId,
        ObjetType objetType,
        String name
) {

    public static ObjetResponseDto of(Objet objet) {
        return new ObjetResponseDto(
                objet.getId(),
                objet.getType(),
                objet.getName()
        );
    }

    public static List<ObjetResponseDto> listOf(List<Objet> objetList) {
        return objetList.stream()
                .map(ObjetResponseDto::of)
                .toList();
    }
}
