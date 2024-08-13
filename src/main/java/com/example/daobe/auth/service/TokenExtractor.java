package com.example.daobe.auth.service;

public interface TokenExtractor {

    Long extractAccessToken(String token);

    String extractRefreshToken(String token);
}
