package com.example.daobe.auth.infrastructure.security;

import static com.example.daobe.auth.infrastructure.security.exception.SecurityExceptionType.INVALID_USER_INFO;

import com.example.daobe.auth.infrastructure.security.exception.SecurityException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BasicAuthenticationProvider implements AuthenticationProvider {

    private static final String ROLE_PREFIX = "ROLE_";

    private final SecurityProperties properties;

    @Override
    public Authentication authenticate(
            Authentication authentication
    ) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        List<SimpleGrantedAuthority> authorityList = extractAuthorityList(name, password);
        return new UsernamePasswordAuthenticationToken(name, password, authorityList);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }

    private List<SimpleGrantedAuthority> extractAuthorityList(
            String name,
            String password
    ) throws AuthenticationException {
        User securityUser = properties.getUser();
        if (isMatchUser(name, password, securityUser)) {
            throw new SecurityException(INVALID_USER_INFO);
        }
        return securityUser.getRoles().stream()
                .map(value -> new SimpleGrantedAuthority(ROLE_PREFIX + value))
                .collect(Collectors.toList());
    }

    private boolean isMatchUser(String name, String password, User securityUser) {
        return !name.equals(securityUser.getName()) ||
                !password.equals(securityUser.getPassword());
    }
}
