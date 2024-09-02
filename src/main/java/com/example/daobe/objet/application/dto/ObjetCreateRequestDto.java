package com.example.daobe.objet.application.dto;

public record ObjetCreateRequestDto(
        String sharers,
        String name,
        String description,
        String type,
        Long loungeId,
        String objetImage
) {
}
