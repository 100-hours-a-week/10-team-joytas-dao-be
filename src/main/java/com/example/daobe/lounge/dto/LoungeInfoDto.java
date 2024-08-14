package com.example.daobe.lounge.dto;

import com.example.daobe.lounge.entity.Lounge;
import lombok.Builder;

@Builder
public record LoungeInfoDto(
        Long loungeId,
        String name,
        String type
) {
    public static LoungeInfoDto of(Lounge lounge) {
        return LoungeInfoDto.builder()
                .loungeId(lounge.getId())
                .name(lounge.getName())
                .type(lounge.getType().getTypeName())
                .build();
    }
}
