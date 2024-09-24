package com.example.daobe.chat.application.dto;

import com.example.daobe.chat.domain.ChatMessage;
import com.example.daobe.chat.domain.ChatUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record ChatMessageInfoDto(
        String id,
        String type,
        @JsonProperty(value = "room_token") String roomToken,
        @JsonProperty(value = "sender_id") Long senderId,
        @JsonProperty(value = "sender_name") String senderName,
        @JsonProperty(value = "sender_profile_url") String senderProfileUrl,
        String message,
        @JsonProperty(value = "created_at") String createdAt
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
                message.getCreatedAt().toString()
        );
    }

    public record EnterAndLeaveMessage(
            String type,
            @JsonProperty(value = "room_token") String roomToken,
            String message,
            @JsonProperty(value = "created_at") String createdAt
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
                    createdAt.toString()
            );
        }
    }
}
