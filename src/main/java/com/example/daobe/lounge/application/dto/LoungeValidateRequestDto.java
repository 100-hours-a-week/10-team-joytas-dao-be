package com.example.daobe.lounge.application.dto;

import com.example.daobe.lounge.domain.Lounge;

public record LoungeValidateRequestDto(Long loungeId) {

    public static LoungeValidateRequestDto of(Lounge lounge) {
        return new LoungeValidateRequestDto(lounge.getId());
    }
}
