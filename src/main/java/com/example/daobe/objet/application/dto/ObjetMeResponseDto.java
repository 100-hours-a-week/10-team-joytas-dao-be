package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;
import com.example.daobe.user.domain.User;
import java.time.LocalDateTime;

public record ObjetMeResponseDto(
        Long objetId,
        String name,
        String objetImage,
        String description,
        ObjetType objetType,
        Long loungeId,
        LocalDateTime createdAt,
        OwnerInfo owner
) {

    public static ObjetMeResponseDto of(Objet objet) {
        return new ObjetMeResponseDto(
                objet.getId(),
                objet.getName(),
                objet.getImageUrl(),
                objet.getExplanation(),
                objet.getType(),
                objet.getLounge().getId(),
                objet.getCreatedAt(),
                OwnerInfo.of(objet.getUser())
        );
    }

    public record OwnerInfo(
            String nickname,
            String profileImage
    ) {

        public static ObjetMeResponseDto.OwnerInfo of(User user) {
            return new ObjetMeResponseDto.OwnerInfo(user.getNickname(), user.getProfileUrl());
        }
    }
}
