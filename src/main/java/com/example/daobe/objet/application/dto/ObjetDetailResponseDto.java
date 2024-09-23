package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;
import java.time.LocalDateTime;

public record ObjetDetailResponseDto(
        Long objetId,
        Long loungeId,
        String name,
        String objetImage,
        String description,
        ObjetType objetType,
        LocalDateTime createdAt,
        OwnerDto owner
) {
    public static ObjetDetailResponseDto of(
            Objet objet
    ) {
        return new ObjetDetailResponseDto(
                objet.getId(),
                objet.getLounge().getId(),
                objet.getName(),
                objet.getImageUrl(),
                objet.getExplanation(),
                objet.getType(),
                objet.getCreatedAt(),
                new OwnerDto(
                        objet.getUser().getId(),
                        objet.getUser().getNickname()
                )
        );
    }

    public record OwnerDto(
            Long userId,
            String nickname
    ) {
    }
}
