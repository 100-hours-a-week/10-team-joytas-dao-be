package com.example.daobe.auth.domain;

import static com.example.daobe.auth.exception.AuthExceptionType.UN_MATCH_USER_INFO;

import com.example.daobe.auth.exception.AuthException;
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

    public void isMatchOrElseThrow(Long userId) {
        if (!Objects.equals(userId, this.userId)) {
            throw new AuthException(UN_MATCH_USER_INFO);
        }
    }

    private String generatedTokenId() {
        return UUID.randomUUID().toString();
    }
}
