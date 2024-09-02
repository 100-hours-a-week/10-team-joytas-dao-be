package com.example.daobe.chat.application;

import static com.example.daobe.chat.domain.MessageType.ENTER;
import static com.example.daobe.chat.domain.MessageType.LEAVE;
import static com.example.daobe.chat.domain.MessageType.TALK;
import static com.example.daobe.chat.exception.ChatExceptionType.INVALID_CHAT_ROOM_ID_EXCEPTION;
import static com.example.daobe.chat.exception.ChatExceptionType.SOCKET_CONNECTION_FAILED_EXCEPTION;

import com.example.daobe.chat.application.dto.ChatMessageDto;
import com.example.daobe.chat.application.dto.ChatMessageDto.EnterAndLeaveMessage;
import com.example.daobe.chat.application.dto.ChatRoomTokenDto;
import com.example.daobe.chat.domain.ChatMessage;
import com.example.daobe.chat.domain.ChatRoom;
import com.example.daobe.chat.domain.repository.ChatMessageRepository;
import com.example.daobe.chat.domain.repository.ChatRoomRepository;
import com.example.daobe.chat.exception.ChatException;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.application.dto.UserInfoResponseDto;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO: 인증 및 예외 처리 로직 작성
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private static final String SENDER_ID = "senderId";
    private static final String SENDER_NAME = "senderName";
    private static final String ROOM_TOKEN = "roomToken";
    private static final int RECENT_MESSAGES_COUNT = 3;

    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatRoom createChatRoom() {
        return chatRoomRepository.save(new ChatRoom());
    }

    public ChatRoomTokenDto getRoomTokenByObjetId(Long objetId) {
        // TODO: 사용자 인증, 오브제 존재 여부 등 검증
        ChatRoom findChatRoom = chatRoomRepository.findChatRoomTokenByObjetId(objetId)
                .orElseThrow(() -> new ChatException(INVALID_CHAT_ROOM_ID_EXCEPTION));
        return new ChatRoomTokenDto(findChatRoom.getRoomToken());
    }

    public EnterAndLeaveMessage createEnterMessageAndSetSessionAttribute(
            ChatMessageDto message,
            String roomToken,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        try {
            Objects.requireNonNull(headerAccessor.getSessionAttributes()).put(SENDER_ID, message.senderId());
            headerAccessor.getSessionAttributes().put(SENDER_NAME, message.senderName());
            headerAccessor.getSessionAttributes().put(ROOM_TOKEN, roomToken);
        } catch (Exception e) {
            throw new ChatException(SOCKET_CONNECTION_FAILED_EXCEPTION);
        }

        UserInfoResponseDto findUser = userService.getUserInfoWithId(message.senderId());
        return EnterAndLeaveMessage.of(
                ENTER.name(),
                message.senderId(),
                roomToken,
                ENTER.getMessage().formatted(findUser.nickname()),
                LocalDateTime.now()
        );
    }

    public ChatMessageDto createMessage(ChatMessageDto message, String roomToken) {
        Long senderId = message.senderId();
        UserInfoResponseDto findUser = userService.getUserInfoWithId(senderId);

        ChatMessage chatMessage = ChatMessage.builder()
                .type(TALK)
                .roomToken(roomToken)
                .senderId(findUser.userId())
                .sender(findUser.nickname())
                .senderProfileUrl(findUser.profileUrl())
                .message(message.message())
                .build();

        // MongoDB 저장
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);
        return ChatMessageDto.of(savedChatMessage);
    }

    public List<ChatMessageDto> getMessagesByRoomToken(String roomToken, boolean isAll) {
        if (isAll) {
            return chatMessageRepository.findAllByRoomToken(roomToken).stream()
                    .map(ChatMessageDto::of)
                    .toList();
        }
        return chatMessageRepository.findAllByRoomTokenOrderByCreatedAtDesc(roomToken).stream()
                .limit(RECENT_MESSAGES_COUNT)
                .map(ChatMessageDto::of)
                .sorted(Comparator.comparing(ChatMessageDto::createdAt))
                .toList();
    }

    public EnterAndLeaveMessage leaveChatRoom(SimpMessageHeaderAccessor headerAccessor) {
        String roomToken = (String) headerAccessor.getSessionAttributes().get(ROOM_TOKEN);
        String senderName = (String) headerAccessor.getSessionAttributes().get(SENDER_NAME);
        Long senderId = (Long) headerAccessor.getSessionAttributes().get(SENDER_ID);

        return EnterAndLeaveMessage.of(
                LEAVE.name(),
                senderId,
                roomToken,
                LEAVE.getMessage().formatted(senderName),
                LocalDateTime.now()
        );
    }
}
