package com.example.daobe.lounge.application.dto;

import com.example.daobe.lounge.domain.Lounge;

public record LoungeInfoDto(
        Long loungeId,
        String name,
        String type
) {
    public static LoungeInfoDto of(Lounge lounge) {
        return new LoungeInfoDto(
                lounge.getId(),
                lounge.getName(),
                lounge.getType().getTypeName()
        );
    }
}
