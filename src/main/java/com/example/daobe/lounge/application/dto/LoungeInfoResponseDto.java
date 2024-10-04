package com.example.daobe.lounge.application.dto;

import com.example.daobe.lounge.domain.Lounge;

public record LoungeInfoResponseDto(
        Long loungeId,
        String name,
        String type
) {

    public static LoungeInfoResponseDto of(Lounge lounge) {
        return new LoungeInfoResponseDto(
                lounge.getId(),
                lounge.getName(),
                lounge.getType().getTypeName()
        );
    }
}
