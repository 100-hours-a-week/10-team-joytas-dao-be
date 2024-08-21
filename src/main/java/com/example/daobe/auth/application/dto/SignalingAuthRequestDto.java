package com.example.daobe.auth.application.dto;

import lombok.Builder;

@Builder
public record SignalingAuthRequestDto(
        Long objetId
) {
}
