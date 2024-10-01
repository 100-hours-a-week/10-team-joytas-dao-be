package com.example.daobe.objet.domain.repository.dto;

public record ObjetListCondition(
        Long loungeId,
        Long userId,
        Long cursor,
        int executeLimitSize
) {
    public ObjetListCondition(Long loungeId, Long cursor, int executeLimitSize) {
        this(loungeId, null, cursor, executeLimitSize);
    }
}
