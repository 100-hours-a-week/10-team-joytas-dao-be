package com.example.daobe.lounge.dto;

import com.example.daobe.lounge.entity.Lounge;

public record LoungeCreateResponseDto(Long loungeId) {
    public static LoungeCreateResponseDto of(Lounge lounge) {

        return new LoungeCreateResponseDto(lounge.getLoungeId());
    }
}
