package com.example.daobe.auth.service;

import com.example.daobe.auth.domain.Token;
import com.example.daobe.auth.dto.TokenResponseDto;
import com.example.daobe.auth.repository.TokenRepository;
import com.example.daobe.user.entity.User;
import com.example.daobe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final TokenExtractor tokenExtractor;

    public TokenResponseDto generateTokenPair(String kakaoId) {
        User findUser = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new RuntimeException("가입하지 않은 아이디"));

        Token newToken = Token.builder()
                .memberId(findUser.getId())
                .build();
        tokenRepository.save(newToken);

        String accessToken = tokenProvider.generatedAccessToken(newToken.getMemberId());
        String refreshToken = tokenProvider.generatedRefreshToken(newToken.getTokenId());
        return TokenResponseDto.of(accessToken, refreshToken);
    }

    public TokenResponseDto reissueTokenPair(String currentToken) {
        String tokenId = tokenExtractor.extractRefreshToken(currentToken);
        Token findToken = tokenRepository.findByTokenId(tokenId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 토큰"));

        tokenRepository.deleteByTokenId(findToken.getTokenId());
        Token newToken = Token.builder()
                .memberId(findToken.getMemberId())
                .build();

        tokenRepository.save(newToken);

        String accessToken = tokenProvider.generatedAccessToken(newToken.getMemberId());
        String refreshToken = tokenProvider.generatedRefreshToken(newToken.getTokenId());
        return TokenResponseDto.of(accessToken, refreshToken);
    }

    public void logout(Long userId, String currentToken) {
        String tokenId = tokenExtractor.extractRefreshToken(currentToken);
        Token findToken = tokenRepository.findByTokenId(tokenId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 토큰"));

        if (!findToken.isMatchMemberId(userId)) {
            throw new RuntimeException("일치하지 않는 유저입니다");
        }

        tokenRepository.deleteByTokenId(findToken.getTokenId());
    }
}
