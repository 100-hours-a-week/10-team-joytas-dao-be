package com.example.daobe.chat.infrastructure.redis;

import static com.example.daobe.chat.exception.ChatExceptionType.CHAT_TRANSFER_FAILED_EXCEPTION;

import com.example.daobe.chat.application.ChatMessageService;
import com.example.daobe.chat.application.dto.ChatMessageInfoDto;
import com.example.daobe.chat.exception.ChatException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessageListener implements MessageListener {

    private final ChatMessageService chatMessageService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String publishedMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
        try {
            ChatMessageInfoDto chatMessage = objectMapper.readValue(publishedMessage, ChatMessageInfoDto.class);
            chatMessageService.publishChatMessage(chatMessage);
        } catch (Exception e) {
            throw new ChatException(CHAT_TRANSFER_FAILED_EXCEPTION);
        }
    }
}
