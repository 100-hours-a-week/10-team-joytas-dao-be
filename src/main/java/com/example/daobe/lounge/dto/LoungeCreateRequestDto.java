package com.example.daobe.lounge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoungeCreateRequestDto(
        String name,

        @JsonProperty("user_id")
        Long userId,

        String type
) {
}
