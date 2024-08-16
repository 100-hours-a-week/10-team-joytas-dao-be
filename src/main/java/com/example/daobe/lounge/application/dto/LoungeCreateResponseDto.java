package com.example.daobe.lounge.application.dto;

import com.example.daobe.lounge.domain.Lounge;

public record LoungeCreateResponseDto(Long loungeId) {
    public static LoungeCreateResponseDto of(Lounge lounge) {

        return new LoungeCreateResponseDto(lounge.getId());
    }
}
