package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;
import java.time.LocalDateTime;

public record ObjetDetailResponseDto(
        Long objetId,
        Long userId,
        Long loungeId,
        String name,
        String nickname,
        String objetImage,
        String description,
        ObjetType objetType,
        LocalDateTime createdAt
) {
    public static ObjetDetailResponseDto of(
            Objet objet
    ) {
        return new ObjetDetailResponseDto(
                objet.getId(),
                objet.getUser().getId(),
                objet.getLounge().getId(),
                objet.getName(),
                objet.getUser().getNickname(),
                objet.getImageUrl(),
                objet.getExplanation(),
                objet.getType(),
                objet.getCreatedAt()
        );

    }
}
