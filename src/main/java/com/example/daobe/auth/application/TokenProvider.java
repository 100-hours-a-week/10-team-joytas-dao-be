package com.example.daobe.auth.application;

public interface TokenProvider {

    String generatedAccessToken(Long memberId);

    String generatedRefreshToken(String tokenId);
}
