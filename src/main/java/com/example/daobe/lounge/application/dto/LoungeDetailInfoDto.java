package com.example.daobe.lounge.application.dto;

import com.example.daobe.lounge.domain.Lounge;

public record LoungeDetailInfoDto(
        Long loungeId,
        String name,
        String type,
        Long userId
) {

    public static LoungeDetailInfoDto of(Lounge lounge) {
        return new LoungeDetailInfoDto(
                lounge.getId(),
                lounge.getName(),
                lounge.getType().getTypeName(),
                lounge.getUser().getId()
        );
    }
}
