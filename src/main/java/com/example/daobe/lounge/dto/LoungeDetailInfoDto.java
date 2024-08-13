package com.example.daobe.lounge.dto;

import java.util.List;

public record LoungeDetailInfoDto(
        Long loungeId,
        String name,
        String type,
        Long userId,
        List<ObjetInfo> objets
) {
    public record ObjetInfo(
            Long objectId,
            String type
    ) {
    }
}
