package com.example.daobe.chat.application.dto;

import java.util.List;

public record ChatMessageResponseDto(
        List<ChatMessageInfoDto> messages,
        boolean hasNext
) {

    public static ChatMessageResponseDto of(List<ChatMessageInfoDto> messages, boolean hasNext) {
        return new ChatMessageResponseDto(messages, hasNext);
    }
}
