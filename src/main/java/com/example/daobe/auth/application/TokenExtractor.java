package com.example.daobe.auth.application;

public interface TokenExtractor {

    Long extractAccessToken(String token);

    String extractRefreshToken(String token);
}
