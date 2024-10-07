package com.example.daobe.chat.application;

import static com.example.daobe.chat.domain.MessageType.ENTER;
import static com.example.daobe.chat.domain.MessageType.LEAVE;
import static com.example.daobe.chat.domain.MessageType.TALK;
import static com.example.daobe.chat.exception.ChatExceptionType.INVALID_CHAT_USER_ID_EXCEPTION;

import com.example.daobe.chat.application.dto.ChatMessageDto;
import com.example.daobe.chat.application.dto.ChatMessageInfoDto;
import com.example.daobe.chat.application.dto.ChatMessageInfoDto.EnterAndLeaveMessage;
import com.example.daobe.chat.domain.ChatMessage;
import com.example.daobe.chat.domain.ChatUser;
import com.example.daobe.chat.domain.MessageType;
import com.example.daobe.chat.domain.repository.ChatMessageRepository;
import com.example.daobe.chat.domain.repository.ChatUserRepository;
import com.example.daobe.chat.exception.ChatException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageEventPublisher messageEventPublisher;

    public void sendChatMessage(Long userId, ChatMessageDto message) {
        ChatMessageInfoDto messageInfoDto = createAndSaveMessage(userId, message);
        messageEventPublisher.execute(messageInfoDto);
    }

    public void sendEnterLeaveMessage(Long userId, ChatMessageDto message) {
        ChatUser findUser = chatUserRepository.findByUserId(userId)
                .orElseThrow(() -> new ChatException(INVALID_CHAT_USER_ID_EXCEPTION));

        String formattedMessage = formatEnterLeaveMessage(message.type(), findUser.getNickname());
        EnterAndLeaveMessage messageInfoDto = createEnterLeaveMessage(message, formattedMessage);
        messageEventPublisher.execute(messageInfoDto);
    }

    private ChatMessageInfoDto createAndSaveMessage(Long userId, ChatMessageDto message) {
        ChatUser findUser = chatUserRepository.findByUserId(userId)
                .orElseThrow(() -> new ChatException(INVALID_CHAT_USER_ID_EXCEPTION));
        ChatMessage chatMessage = ChatMessage.builder()
                .type(TALK)
                .roomToken(message.roomToken())
                .senderId(userId)
                .message(message.message())
                .build();
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);
        return ChatMessageInfoDto.of(savedChatMessage, findUser);
    }

    private String formatEnterLeaveMessage(MessageType type, String nickname) {
        return (type.equals(ENTER) ? ENTER.getMessage() : LEAVE.getMessage())
                .formatted(nickname);
    }

    private EnterAndLeaveMessage createEnterLeaveMessage(ChatMessageDto messageDto, String message) {
        return EnterAndLeaveMessage.of(
                messageDto.type().name(),
                messageDto.roomToken(),
                message,
                LocalDateTime.now()
        );
    }
}
