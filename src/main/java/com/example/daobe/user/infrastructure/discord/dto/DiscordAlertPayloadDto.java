package com.example.daobe.user.infrastructure.discord.dto;

import java.util.List;

public record DiscordAlertPayloadDto(
        String content,
        List<Embeds> embeds
) {

    public static DiscordAlertPayloadDto of(String content, String title, String description) {
        return new DiscordAlertPayloadDto(content, List.of(new Embeds(title, description)));
    }

    // Nested
    public record Embeds(
            String title,
            String description
    ) {
    }
}
