package com.example.daobe.user.infrastructure.discord;

import com.example.daobe.user.infrastructure.discord.dto.DiscordAlertPayloadDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface DiscordApiClient {

    @PostExchange
    void execute(@RequestBody DiscordAlertPayloadDto payload);
}
