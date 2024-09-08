package com.example.daobe.auth.domain;

import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

    private Long userId;
    private String tokenId;

    @Builder
    public Token(Long userId) {
        this.userId = userId;
        this.tokenId = generatedTokenId();
    }

    public boolean isMatchUserId(Long userId) {
        return Objects.equals(userId, this.userId);
    }

    private String generatedTokenId() {
        return UUID.randomUUID().toString();
    }
}
