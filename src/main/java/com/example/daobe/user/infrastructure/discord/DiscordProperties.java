package com.example.daobe.user.infrastructure.discord;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discord")
public record DiscordProperties(
        String webhookUrl,
        String messageFormat
) {
}
