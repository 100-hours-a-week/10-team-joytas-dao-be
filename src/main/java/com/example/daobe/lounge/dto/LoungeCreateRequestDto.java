package com.example.daobe.lounge.dto;

public record LoungeCreateRequestDto(
        String name,
        Long userId,
        String type
) {
}
