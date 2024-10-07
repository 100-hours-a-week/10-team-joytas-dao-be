package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;
import com.example.daobe.user.domain.User;
import java.time.LocalDateTime;

public record ObjetDetailResponseDto(
        Long objetId,
        Long loungeId,
        String name,
        String objetImage,
        String description,
        ObjetType objetType,
        LocalDateTime createdAt,
        OwnerInfo owner
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
                OwnerInfo.of(objet.getUser())
        );
    }

    public record OwnerInfo(
            Long userId,
            String nickname
    ) {

        public static ObjetDetailResponseDto.OwnerInfo of(User user) {
            return new ObjetDetailResponseDto.OwnerInfo(user.getId(), user.getNickname());
        }
    }
}
