package com.example.daobe.objet.domain.repository.dto;

public record ObjetFindCondition(
        Long loungeId,
        Long userId,
        Long cursor,
        int executeLimitSize
) {

    public static ObjetFindCondition of(long loungeId, long userId, long cursor, int executeLimitSize) {
        return new ObjetFindCondition(loungeId, userId, cursor, executeLimitSize);
    }

    public static ObjetFindCondition of(long loungeId, long cursor, int executeLimitSize) {
        return new ObjetFindCondition(loungeId, null, cursor, executeLimitSize);
    }
}
