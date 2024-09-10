package com.example.daobe.chat.application.dto;

public record ChatMessageDto(
        Long senderId,
        String message
) {
    public static ChatMessageDto of(Long senderId, String message) {
        return new ChatMessageDto(senderId, message);
    }
}
