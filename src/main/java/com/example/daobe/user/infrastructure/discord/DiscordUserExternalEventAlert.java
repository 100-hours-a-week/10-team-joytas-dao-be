package com.example.daobe.user.infrastructure.discord;

import com.example.daobe.user.application.UserExternalEventAlert;
import com.example.daobe.user.domain.event.UserInquiriesEvent;
import com.example.daobe.user.infrastructure.discord.dto.DiscordAlertPayloadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscordUserExternalEventAlert implements UserExternalEventAlert {

    private static final String DISCORD_MESSAGE_TITLE = "💬 1:1 문의하기";
    private static final String DISCORD_MESSAGE_CONTENTS = "## 🚀 새로운 문의가 도착했습니다!";

    private final DiscordApiClient discordApiClient;
    private final DiscordProperties discordProperties;

    @Override
    public void execute(UserInquiriesEvent event) {
        String description = String.format(
                discordProperties.messageFormat(),
                event.userId(),
                event.nickname(),
                event.email(),
                event.contents()
        );
        DiscordAlertPayloadDto payload = DiscordAlertPayloadDto.of(
                DISCORD_MESSAGE_CONTENTS,
                DISCORD_MESSAGE_TITLE,
                description
        );
        discordApiClient.execute(payload);
    }
}
