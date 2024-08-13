package com.example.daobe.auth.service;

public interface TokenProvider {

    String generatedAccessToken(Long memberId);

    String generatedRefreshToken(String tokenId);
}
