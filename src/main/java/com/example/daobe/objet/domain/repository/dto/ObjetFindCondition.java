package com.example.daobe.objet.domain.repository.dto;

public record ObjetFindCondition(
        Long loungeId,
        Long userId,
        Long cursor,
        int executeLimitSize
) {

    public static ObjetFindCondition of(Long loungeId, Long userId, Long cursor, int executeLimitSize) {
        return new ObjetFindCondition(loungeId, userId, cursor, executeLimitSize);
    }

    public static ObjetFindCondition of(Long loungeId, Long cursor, int executeLimitSize) {
        return new ObjetFindCondition(loungeId, null, cursor, executeLimitSize);
    }
}
