package com.example.daobe.chat.application.dto;

import com.example.daobe.chat.domain.ChatMessage;
import com.example.daobe.chat.domain.ChatUser;
import java.time.LocalDateTime;

public record ChatMessageInfoDto(
        String id,
        String type,
        String roomToken,
        Long senderId,
        String senderName,
        String senderProfileUrl,
        String message,
        LocalDateTime createdAt
) {
    public static ChatMessageInfoDto of(ChatMessage message, ChatUser sender) {
        return new ChatMessageInfoDto(
                message.getId().toHexString(),
                message.getType(),
                message.getRoomToken(),
                message.getSenderId(),
                sender.getNickname(),
                sender.getProfileUrl(),
                message.getMessage(),
                message.getCreatedAt()
        );
    }

    public record EnterAndLeaveMessage(
            String type,
            String roomToken,
            String message,
            LocalDateTime createdAt
    ) {
        public static EnterAndLeaveMessage of(
                String type,
                String roomToken,
                String message,
                LocalDateTime createdAt
        ) {
            return new EnterAndLeaveMessage(
                    type,
                    roomToken,
                    message,
                    createdAt
            );
        }
    }
}
