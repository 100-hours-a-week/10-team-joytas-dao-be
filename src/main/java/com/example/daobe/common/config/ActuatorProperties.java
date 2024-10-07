package com.example.daobe.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.actuator")
public record ActuatorProperties(
        String username,
        String password,
        String role
) {
}
