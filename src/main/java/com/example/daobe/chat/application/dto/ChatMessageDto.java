package com.example.daobe.chat.application.dto;

import com.example.daobe.chat.domain.ChatMessage;
import java.time.LocalDateTime;

public record ChatMessageDto(
        String id,
        String type,
        String roomToken,
        Long senderId,
        String senderName,
        String senderProfileUrl,
        String message,
        LocalDateTime createdAt
) {
    public static ChatMessageDto of(ChatMessage message) {
        return new ChatMessageDto(
                message.getId(),
                message.getType(),
                message.getRoomToken(),
                message.getSenderId(),
                message.getSender(),
                message.getSenderProfileUrl(),
                message.getMessage(),
                message.getCreatedAt()
        );
    }

    public record EnterAndLeaveMessage(
            String type,
            Long senderId,
            String roomToken,
            String message,
            LocalDateTime createdAt
    ) {
        public static EnterAndLeaveMessage of(
                String type,
                Long senderId,
                String roomToken,
                String message,
                LocalDateTime createdAt
        ) {
            return new EnterAndLeaveMessage(
                    type,
                    senderId,
                    roomToken,
                    message,
                    createdAt
            );
        }
    }
}
