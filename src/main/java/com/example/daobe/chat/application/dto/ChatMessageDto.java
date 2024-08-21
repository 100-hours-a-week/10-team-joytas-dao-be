package com.example.daobe.chat.application.dto;

public record ChatRequestDto(
        MessageType type,
        Long chatRoomId,
        String sender,
        String message

) {
    public enum MessageType {
        ENTER, TALK, LEAVE;
    }
}
