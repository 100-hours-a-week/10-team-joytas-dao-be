package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;
import java.time.LocalDateTime;
import java.util.List;

public record ObjetResponseDto(
        Long objetId,
        ObjetType objetType,
        String name,
        String objetImage,
        String description,
        LocalDateTime createdAt,
        String nickname,
        String profileImage
) {

    public static ObjetResponseDto of(Objet objet) {
        return new ObjetResponseDto(
                objet.getId(),
                objet.getType(),
                objet.getName(),
                objet.getImageUrl(),
                objet.getExplanation(),
                objet.getCreatedAt(),
                objet.getUser().getNickname(),
                objet.getUser().getProfileUrl()
        );
    }

    public static List<ObjetResponseDto> listOf(List<Objet> objetList) {
        return objetList.stream()
                .map(ObjetResponseDto::of)
                .toList();
    }
}
