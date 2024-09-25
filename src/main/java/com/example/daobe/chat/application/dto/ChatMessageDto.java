package com.example.daobe.chat.application.dto;

import com.example.daobe.chat.domain.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatMessageDto(
        String message,
        MessageType type,
        @JsonProperty("room_token") String roomToken
) {

    public static ChatMessageDto of(String message, MessageType type, String roomToken) {
        return new ChatMessageDto(message, type, roomToken);
    }
}
