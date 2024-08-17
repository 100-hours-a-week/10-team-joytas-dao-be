package com.example.daobe.objet.application.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ObjetUpdateRequestDto(
        Long objetId,
        List<Long> owners,
        String name,
        String description
) {
}
