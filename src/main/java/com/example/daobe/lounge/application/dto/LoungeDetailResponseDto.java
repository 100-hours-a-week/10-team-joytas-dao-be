package com.example.daobe.lounge.application.dto;

import com.example.daobe.lounge.domain.Lounge;

public record LoungeDetailResponseDto(
        Long loungeId,
        String name,
        String type,
        Long userId
) {

    public static LoungeDetailResponseDto of(Lounge lounge) {
        return new LoungeDetailResponseDto(
                lounge.getId(),
                lounge.getName(),
                lounge.getType().getTypeName(),
                lounge.getUser().getId()
        );
    }
}
