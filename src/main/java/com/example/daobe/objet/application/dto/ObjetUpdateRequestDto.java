package com.example.daobe.objet.application.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ObjetUpdateRequestDto(
        Long objetId,
        List<Long> sharers,
        String name,
        String description
) {
}
