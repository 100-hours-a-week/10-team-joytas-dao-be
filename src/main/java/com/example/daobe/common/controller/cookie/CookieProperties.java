package com.example.daobe.common.controller.cookie;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cookie")
public record CookieProperties(
        Long maxAge,
        String path,
        String sameSite,
        String domain,
        boolean httpOnly,
        boolean secure
) {
}
