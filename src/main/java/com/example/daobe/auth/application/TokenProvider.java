package com.example.daobe.auth.application;

public interface TokenProvider {

    String generatedAccessToken(Long userId);

    String generatedRefreshToken(String tokenId);
}
