package com.example.daobe.lounge.application.dto;

import com.example.daobe.lounge.domain.Lounge;

public record LoungeDto(Long loungeId) {

    public static LoungeDto of(Lounge lounge) {
        return new LoungeDto(lounge.getId());
    }
}
