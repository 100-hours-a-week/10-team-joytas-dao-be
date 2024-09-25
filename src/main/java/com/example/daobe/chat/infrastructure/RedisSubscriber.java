package com.example.daobe.chat.infrastructure;

import static com.example.daobe.chat.exception.ChatExceptionType.CHAT_TRANSFER_FAILED_EXCEPTION;

import com.example.daobe.chat.application.dto.ChatMessageInfoDto;
import com.example.daobe.chat.exception.ChatException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String publishedMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
        try {
            ChatMessageInfoDto chatMessage = objectMapper.readValue(publishedMessage, ChatMessageInfoDto.class);
            messagingTemplate.convertAndSend("/sub/chat-rooms/" + chatMessage.roomToken(), chatMessage);
        } catch (Exception e) {
            throw new ChatException(CHAT_TRANSFER_FAILED_EXCEPTION);
        }
    }
}
