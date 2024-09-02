package com.example.daobe.objet.application.dto;

import lombok.Builder;

@Builder
public record ObjetUpdateRequestDto(
        Long objetId,
        String sharers,
        String name,
        String description,
        String objetImage
) {
}
