package com.example.daobe.auth.application;

import static com.example.daobe.auth.exception.AuthExceptionType.INVALID_TOKEN;
import static com.example.daobe.auth.exception.AuthExceptionType.UN_MATCH_USER_INFO;

import com.example.daobe.auth.application.dto.TokenResponseDto;
import com.example.daobe.auth.domain.Token;
import com.example.daobe.auth.domain.repository.TokenRepository;
import com.example.daobe.auth.exception.AuthException;
import com.example.daobe.user.entity.User;
import com.example.daobe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final TokenExtractor tokenExtractor;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public TokenResponseDto loginOrRegister(String oAuthId) {
        User findUser = userRepository.findByKakaoId(oAuthId)
                .orElseGet(() -> userRepository.save(generatedUser(oAuthId)));

        Token newToken = Token.builder()
                .memberId(findUser.getId())
                .build();
        tokenRepository.save(newToken);

        String accessToken = tokenProvider.generatedAccessToken(newToken.getMemberId());
        String refreshToken = tokenProvider.generatedRefreshToken(newToken.getTokenId());
        return TokenResponseDto.of(accessToken, refreshToken);
    }

    private User generatedUser(String oAuthId) {
        return User.builder().kakaoId(oAuthId).build();
    }

    public TokenResponseDto reissueTokenPair(String currentToken) {
        String tokenId = tokenExtractor.extractRefreshToken(currentToken);
        Token findToken = tokenRepository.findByTokenId(tokenId)
                .orElseThrow(() -> new AuthException(INVALID_TOKEN));

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
                .orElseThrow(() -> new AuthException(INVALID_TOKEN));

        if (!findToken.isMatchMemberId(userId)) {
            throw new AuthException(UN_MATCH_USER_INFO);
        }

        tokenRepository.deleteByTokenId(findToken.getTokenId());
    }
}
