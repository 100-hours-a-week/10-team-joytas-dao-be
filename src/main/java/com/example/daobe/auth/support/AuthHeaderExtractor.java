package com.example.daobe.auth.support;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

public class AuthHeaderExtractor {

    private static final String BEARER_PREFIX = "Bearer";

    public static Optional<String> extract(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (
                StringUtils.hasText(header) &&
                        header.startsWith(BEARER_PREFIX)
        ) {
            return Optional.of(header.substring(BEARER_PREFIX.length()).trim());
        }
        return Optional.empty();
    }
}
