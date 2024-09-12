package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetType;
import java.util.List;

public record ObjetDetailResponseDto(
        Long objetId,
        Long userId,
        Long loungeId,
        String name,
        String nickname,
        String objetImage,
        String description,
        ObjetType objetType,
        Boolean isActive,
        Long callingUserNum,
        List<ViewerInfo> viewers,
        List<SharerInfo> sharers
) {
    public static ObjetDetailResponseDto of(
            Objet objet,
            boolean isActive,
            Long callingUserNum,
            List<ViewerInfo> viewers,
            List<SharerInfo> sharers
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
                isActive,
                callingUserNum,
                viewers,
                sharers
        );

    }

    public record SharerInfo(
            Long userId,
            String nickname
    ) {
        public static SharerInfo of(Long userId, String nickname) {
            return new SharerInfo(
                    userId,
                    nickname
            );
        }
    }

    public record ViewerInfo(
            Long userId,
            String profileImage
    ) {
        public static ViewerInfo of(Long userId, String profileImage) {
            return new ViewerInfo(userId, profileImage);
        }
    }
}
