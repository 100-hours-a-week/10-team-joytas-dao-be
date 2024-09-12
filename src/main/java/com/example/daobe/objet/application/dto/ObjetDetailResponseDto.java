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
        Long callingUserNum,
        List<SharerInfo> sharers
) {
    public static ObjetDetailResponseDto of(
            Objet objet,
            Long callingUserNum,
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
                callingUserNum,
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
}
