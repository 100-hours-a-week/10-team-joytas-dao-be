package com.example.daobe.auth.application;

import static com.example.daobe.auth.exception.AuthExceptionType.INVALID_TOKEN;

import com.example.daobe.auth.application.dto.TokenResponseDto;
import com.example.daobe.auth.domain.Token;
import com.example.daobe.auth.domain.repository.TokenRepository;
import com.example.daobe.auth.exception.AuthException;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final TokenExtractor tokenExtractor;
    private final TokenRepository tokenRepository;

    public TokenResponseDto loginOrRegister(String oAuthId) {
        User findUser = userService.getOrRegisterByOAuthId(oAuthId);

        Token newToken = Token.builder()
                .userId(findUser.getId())
                .build();
        tokenRepository.save(newToken);

        String accessToken = tokenProvider.generatedAccessToken(newToken.getUserId());
        String refreshToken = tokenProvider.generatedRefreshToken(newToken.getTokenId());
        return TokenResponseDto.of(accessToken, refreshToken);
    }

    public TokenResponseDto reissueTokenPair(String currentToken) {
        String tokenId = tokenExtractor.extractRefreshToken(currentToken);
        Token findToken = tokenRepository.findByTokenId(tokenId)
                .orElseThrow(() -> new AuthException(INVALID_TOKEN));

        tokenRepository.deleteByTokenId(findToken.getTokenId());
        Token newToken = Token.builder()
                .userId(findToken.getUserId())
                .build();

        tokenRepository.save(newToken);

        String accessToken = tokenProvider.generatedAccessToken(newToken.getUserId());
        String refreshToken = tokenProvider.generatedRefreshToken(newToken.getTokenId());
        return TokenResponseDto.of(accessToken, refreshToken);
    }

    public void logout(Long userId, String currentToken) {
        String tokenId = tokenExtractor.extractRefreshToken(currentToken);
        Token findToken = tokenRepository.findByTokenId(tokenId)
                .orElseThrow(() -> new AuthException(INVALID_TOKEN));

        findToken.isMatchOrElseThrow(userId);

        tokenRepository.deleteByTokenId(findToken.getTokenId());
    }
}
