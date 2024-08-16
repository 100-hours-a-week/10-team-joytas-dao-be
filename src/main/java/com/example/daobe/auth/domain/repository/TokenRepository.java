package com.example.daobe.auth.domain.repository;

import com.example.daobe.auth.domain.Token;
import java.util.Optional;

public interface TokenRepository {

    void save(Token token);

    void deleteByTokenId(String tokenId);

    Optional<Token> findByTokenId(String tokenId);
}
