package com.example.daobe.lounge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoungeCreateResponseDto(
        @JsonProperty("lounge_id")
        Long loungeId
) {
}
