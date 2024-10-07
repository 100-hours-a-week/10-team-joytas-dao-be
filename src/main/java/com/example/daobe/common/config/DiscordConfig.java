package com.example.daobe.common.config;

import com.example.daobe.user.infrastructure.discord.DiscordApiClient;
import com.example.daobe.user.infrastructure.discord.DiscordProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class DiscordConfig {

    private final DiscordProperties discordProperties;

    @Bean
    public DiscordApiClient createHttpInterface() {
        RestClient restClient = RestClient.builder().baseUrl(discordProperties.webhookUrl()).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(DiscordApiClient.class);
    }
}
