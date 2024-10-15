package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;
import com.example.daobe.user.domain.User;
import java.time.LocalDateTime;

public record ObjetResponseDto(
        Long objetId,
        ObjetType objetType,
        String name,
        String objetImage,
        String description,
        LocalDateTime createdAt,
        OwnerInfo owner
) {

    public static ObjetResponseDto of(Objet objet) {
        return new ObjetResponseDto(
                objet.getId(),
                objet.getType(),
                objet.getName(),
                objet.getImageUrl(),
                objet.getExplanation(),
                objet.getCreatedAt(),
                OwnerInfo.of(objet.getUser())
        );
    }

    // Nested
    private record OwnerInfo(
            String nickname,
            String profileImage
    ) {

        public static OwnerInfo of(User user) {
            return new OwnerInfo(user.getNickname(), user.getProfileUrl());
        }
    }
}
