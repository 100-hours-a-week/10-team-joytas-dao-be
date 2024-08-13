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

    private Long memberId;
    private String tokenId;

    @Builder
    public Token(Long memberId) {
        this.memberId = memberId;
        this.tokenId = generatedTokenId();
    }

    public boolean isMatchMemberId(Long memberId) {
        return Objects.equals(memberId, this.memberId);
    }

    private String generatedTokenId() {
        return UUID.randomUUID().toString();
    }
}
