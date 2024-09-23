package com.example.daobe.chat.application;

import static com.example.daobe.chat.domain.MessageType.ENTER;
import static com.example.daobe.chat.domain.MessageType.LEAVE;
import static com.example.daobe.chat.domain.MessageType.TALK;
import static com.example.daobe.chat.exception.ChatExceptionType.INVALID_CHAT_ROOM_ID_EXCEPTION;
import static com.example.daobe.chat.exception.ChatExceptionType.INVALID_CHAT_USER_ID_EXCEPTION;
import static com.example.daobe.chat.exception.ChatExceptionType.SOCKET_CONNECTION_FAILED_EXCEPTION;

import com.example.daobe.chat.application.dto.ChatMessageDto;
import com.example.daobe.chat.application.dto.ChatMessageInfoDto;
import com.example.daobe.chat.application.dto.ChatMessageInfoDto.EnterAndLeaveMessage;
import com.example.daobe.chat.application.dto.ChatMessageResponseDto;
import com.example.daobe.chat.application.dto.ChatRoomTokenDto;
import com.example.daobe.chat.domain.ChatMessage;
import com.example.daobe.chat.domain.ChatRoom;
import com.example.daobe.chat.domain.ChatUser;
import com.example.daobe.chat.domain.repository.ChatMessageRepository;
import com.example.daobe.chat.domain.repository.ChatRoomRepository;
import com.example.daobe.chat.domain.repository.ChatUserRepository;
import com.example.daobe.chat.exception.ChatException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private static final String MESSAGE_SORT_FIELD_CREATED_AT = "createdAt";
    private static final int RECENT_MESSAGES_COUNT = 3;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoomTokenDto getRoomTokenByObjetId(Long objetId) {
        // TODO: 사용자 인증, 오브제 존재 여부 등 검증
        ChatRoom findChatRoom = chatRoomRepository.findChatRoomTokenByObjetId(objetId)
                .orElseThrow(() -> new ChatException(INVALID_CHAT_ROOM_ID_EXCEPTION));
        return new ChatRoomTokenDto(findChatRoom.getRoomToken());
    }

    public EnterAndLeaveMessage createEnterMessageAndSetSessionAttribute(
            ChatMessageInfoDto message,
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

        ChatUser findUser = chatUserRepository.findByUserId(message.senderId())
                .orElseThrow(() -> new ChatException(INVALID_CHAT_USER_ID_EXCEPTION));
        return EnterAndLeaveMessage.of(
                ENTER.name(),
                roomToken,
                ENTER.getMessage().formatted(findUser.getNickname()),
                LocalDateTime.now()
        );
    }

    public ChatMessageInfoDto createMessage(ChatMessageDto message, String roomToken) {
        ChatUser findUser = chatUserRepository.findByUserId(message.senderId())
                .orElseThrow(() -> new ChatException(INVALID_CHAT_USER_ID_EXCEPTION));
        ChatMessage chatMessage = ChatMessage.builder()
                .type(TALK)
                .roomToken(roomToken)
                .senderId(findUser.getUserId())
                .message(message.message())
                .build();
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);
        return ChatMessageInfoDto.of(savedChatMessage, findUser);
    }

    public ChatMessageResponseDto getMessagesByRoomToken(String roomToken, ObjectId cursorId, int limit) {
        List<ChatMessage> pagedChatMessages = getPagedChatMessages(roomToken, cursorId, limit);
        boolean hasNext = checkHasNextPage(pagedChatMessages, limit);
        List<ChatMessage> chatMessages = getChatMessagesToLimit(pagedChatMessages, hasNext, limit);
        Map<Long, ChatUser> userInfos = getUserInfoForMessages(chatMessages);
        return mapMessagesToDto(chatMessages, userInfos, hasNext);
    }

    public ChatMessageResponseDto getRecentMessagesByRoomToken(String roomToken) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomTokenOrderByCreatedAtDesc(roomToken)
                .stream()
                .limit(RECENT_MESSAGES_COUNT)
                .toList();
        Map<Long, ChatUser> userInfos = getUserInfoForMessages(chatMessages);
        return mapMessagesToDto(chatMessages, userInfos, false);
    }

    public EnterAndLeaveMessage leaveChatRoom(SimpMessageHeaderAccessor headerAccessor) {
        String roomToken = (String) headerAccessor.getSessionAttributes().get(ROOM_TOKEN);
        String senderName = (String) headerAccessor.getSessionAttributes().get(SENDER_NAME);

        return EnterAndLeaveMessage.of(
                LEAVE.name(),
                roomToken,
                LEAVE.getMessage().formatted(senderName),
                LocalDateTime.now()
        );
    }

    private List<ChatMessage> getPagedChatMessages(String roomToken, ObjectId cursorId, int limit) {
        PageRequest pageRequest = PageRequest.of(
                0,
                limit + 1,
                Sort.by(Sort.Direction.DESC, MESSAGE_SORT_FIELD_CREATED_AT)
        );

        if (cursorId == null) {
            return chatMessageRepository.findByRoomToken(roomToken, pageRequest).getContent();
        }
        return chatMessageRepository.findByRoomTokenAndIdLessThan(roomToken, cursorId, pageRequest).getContent();
    }

    private boolean checkHasNextPage(List<ChatMessage> chatMessages, int limit) {
        return chatMessages.size() > limit;
    }

    private List<ChatMessage> getChatMessagesToLimit(List<ChatMessage> chatMessages, boolean hasNext, int limit) {
        if (hasNext) {
            return chatMessages.subList(0, limit);
        }
        return chatMessages;
    }

    // TODO: Redis 활용한 캐싱 처리 필요
    private Map<Long, ChatUser> getUserInfoForMessages(List<ChatMessage> chatMessages) {
        Set<Long> userIds = chatMessages.stream()
                .map(ChatMessage::getSenderId)
                .collect(Collectors.toSet());
        List<ChatUser> chatUsers = chatUserRepository.findAllByUserIdIn(userIds);
        return chatUsers.stream()
                .collect(Collectors.toMap(ChatUser::getUserId, Function.identity()));
    }

    private ChatMessageResponseDto mapMessagesToDto(
            List<ChatMessage> chatMessages,
            Map<Long, ChatUser> userInfos,
            boolean hasNext
    ) {
        List<ChatMessageInfoDto> chatMessageInfoDtos = chatMessages.stream()
                .map(chatMessage -> {
                    ChatUser chatUser = userInfos.get(chatMessage.getSenderId());
                    return ChatMessageInfoDto.of(chatMessage, chatUser);
                })
                .sorted(Comparator.comparing(ChatMessageInfoDto::createdAt))
                .toList();
        return ChatMessageResponseDto.of(chatMessageInfoDtos, hasNext);
    }
}
