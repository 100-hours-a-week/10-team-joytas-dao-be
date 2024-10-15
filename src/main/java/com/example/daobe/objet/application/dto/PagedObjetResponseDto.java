package com.example.daobe.objet.application.dto;

import com.example.daobe.objet.domain.Objet;
import java.util.List;

public record PagedObjetResponseDto(
        boolean hasNext,
        Object nextCursor,
        List<ObjetResponseDto> objets
) {

    public static PagedObjetResponseDto of(boolean hasNext, List<Objet> objetList) {
        return new PagedObjetResponseDto(
                hasNext,
                null,
                objetList.stream()
                        .map(ObjetResponseDto::of)
                        .toList()
        );
    }

    public static PagedObjetResponseDto of(boolean hasNext, Object nextCursor, List<Objet> objetList) {
        return new PagedObjetResponseDto(
                hasNext,
                nextCursor,
                objetList.stream()
                        .map(ObjetResponseDto::of)
                        .toList()
        );
    }
}
