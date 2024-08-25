package com.example.daobe.chat.application;

import static com.example.daobe.chat.domain.MessageType.ENTER;
import static com.example.daobe.chat.domain.MessageType.TALK;

import com.example.daobe.chat.application.dto.ChatMessageDto;
import com.example.daobe.chat.application.dto.ChatRoomTokenDto;
import com.example.daobe.chat.domain.ChatMessage;
import com.example.daobe.chat.domain.ChatRoom;
import com.example.daobe.chat.domain.repository.ChatMessageRepository;
import com.example.daobe.chat.domain.repository.ChatRoomRepository;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.application.dto.UserInfoResponseDto;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    public ChatRoomTokenDto getRoomTokenByObjetId(Long objetId) {
        // TODO: 사용자 인증, 오브제 존재 여부 등 검증
        ChatRoom findChatRoom = chatRoomRepository.findChatRoomTokenByObjetId(objetId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 채팅방"));
        return new ChatRoomTokenDto(findChatRoom.getRoomToken());
    }

    public ChatMessageDto.EnterMessage createEnterMessageAndSetSessionAttribute(
            ChatMessageDto message,
            String roomToken,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // 세션 속성이 없는 경우, 재인증 또는 새로운 세션 생성 처리
        if (headerAccessor.getSessionAttributes() == null) {
            log.warn("Session attributes are missing, reinitializing session or requesting re-authentication.");
            headerAccessor.setSessionAttributes(new HashMap<>()); // 새로운 세션 초기화
        }

        // 세션 속성 업데이트
        Optional.ofNullable(headerAccessor.getSessionAttributes())
                .ifPresent(sessionAttributes -> {
                    if (message.senderName() != null) {
                        sessionAttributes.put("senderId", message.senderId());
                    }
                    if (message.roomToken() != null) {
                        sessionAttributes.put("roomId", message.roomToken());
                    }
                });

        UserInfoResponseDto findUser = userService.getUserInfoWithId(message.senderId());
        if (findUser == null) {
            throw new IllegalArgumentException("Invalid user ID: " + message.senderId());
        }

        String welcomeMessage = findUser.nickname() + "님이 입장하셨습니다.";

        return new ChatMessageDto.EnterMessage(
                ENTER.name(),
                roomToken,
                welcomeMessage,
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

        return new ChatMessageDto(
                savedChatMessage.getId(),
                savedChatMessage.getType(),
                savedChatMessage.getRoomToken(),
                savedChatMessage.getSenderId(),
                savedChatMessage.getSender(),
                savedChatMessage.getSenderProfileUrl(),
                savedChatMessage.getMessage(),
                savedChatMessage.getCreatedAt()
        );
    }

    public List<ChatMessageDto> getMessagesByRoomToken(String roomToken, boolean isAll) {
        if (isAll) {
            return chatMessageRepository.findAllByRoomToken(roomToken).stream()
                    .map(ChatMessageDto::of)
                    .toList();
        }
        return chatMessageRepository.findAllByRoomTokenOrderByCreatedAtDesc(roomToken).stream()
                .limit(3)
                .map(ChatMessageDto::of)
                .sorted(Comparator.comparing(ChatMessageDto::createdAt))
                .toList();
    }

}
