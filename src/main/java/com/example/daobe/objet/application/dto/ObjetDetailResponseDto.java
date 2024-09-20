package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharer;
import com.example.daobe.objet.domain.ObjetType;
import java.time.LocalDateTime;
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
        List<SharerInfo> sharers,
        LocalDateTime createdAt
) {
    public static ObjetDetailResponseDto of(
            Objet objet,
            Long callingUserNum,
            List<ObjetSharer> objetSharerList
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
                SharerInfo.listOf(objetSharerList),
                objet.getCreatedAt()
        );

    }

    // Nested
    private record SharerInfo(
            Long userId,
            String nickname
    ) {
        public static SharerInfo of(Long userId, String nickname) {
            return new SharerInfo(
                    userId,
                    nickname
            );
        }

        public static List<SharerInfo> listOf(List<ObjetSharer> objetSharerList) {
            return objetSharerList.stream()
                    .map((objetSharer) -> SharerInfo.of(objetSharer.getUser().getId(),
                            objetSharer.getUser().getNickname()))
                    .toList();
        }
    }
}
