package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.ObjetSharer;
import java.util.List;

public record ObjetSharerResponseDto(
        List<SharerInfo> sharers
) {

    public static ObjetSharerResponseDto of(
            List<ObjetSharer> objetSharerList
    ) {
        return new ObjetSharerResponseDto(
                SharerInfo.listOf(objetSharerList)
        );
    }


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
