package com.example.daobe.chat.presentation;

import com.example.daobe.chat.application.ChatService;
import com.example.daobe.chat.application.dto.ChatMessageDto;
import com.example.daobe.chat.application.dto.ChatMessageInfoDto;
import com.example.daobe.chat.application.dto.ChatMessageResponseDto;
import com.example.daobe.chat.application.dto.ChatRoomTokenDto;
import com.example.daobe.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private static final String SUBSCRIBE_URL = "/sub/chat-rooms/%s/messages";

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat-rooms/{roomToken}/enter")
    public void enterChatRoom(
            @DestinationVariable("roomToken") String token,
            @Payload ChatMessageInfoDto message,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        ChatMessageInfoDto.EnterAndLeaveMessage newMessage = chatService.createEnterMessageAndSetSessionAttribute(
                message,
                token,
                headerAccessor
        );
        messagingTemplate.convertAndSend(SUBSCRIBE_URL.formatted(token), newMessage);
    }

    @MessageMapping("/chat-rooms/{roomToken}/messages")
    public void sendMessage(
            @DestinationVariable("roomToken") String token,
            @Payload ChatMessageDto message
    ) {
        log.info("payload: {}", message.toString());
        messagingTemplate.convertAndSend(
                SUBSCRIBE_URL.formatted(token),
                chatService.createMessage(message, token)
        );
    }

    @MessageMapping("/chat-rooms/{roomToken}/exit")
    public void leaveChatRoom(
            @DestinationVariable("roomToken") String token,
            SimpMessageHeaderAccessor simpMessageHeaderAccessor
    ) {
        messagingTemplate.convertAndSend(
                SUBSCRIBE_URL.formatted(token),
                chatService.leaveChatRoom(simpMessageHeaderAccessor)
        );
    }

    @GetMapping("/api/v1/chat-rooms/{objetId}/room-token")
    public ResponseEntity<ApiResponse<ChatRoomTokenDto>> getChatRoomToken(
            @PathVariable(name = "objetId") Long objetId
    ) {
        // TODO: 오브제에 소속된 사람만 조회 가능하도록 수정
        ChatRoomTokenDto roomToken = chatService.getRoomTokenByObjetId(objetId);
        ApiResponse<ChatRoomTokenDto> response = new ApiResponse<>(
                "CHAT_ROOM_TOKEN_LOADED_SUCCESS",
                roomToken
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/v1/chat-rooms/{roomToken}/messages")
    public ResponseEntity<ApiResponse<ChatMessageResponseDto>> getChatMessages(
            @PathVariable(value = "roomToken") String token,
            @RequestParam(value = "cursorId", required = false) ObjectId cursorId,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        "MESSAGES_LOADED_SUCCESS",
                        chatService.getMessagesByRoomToken(token, cursorId, limit)
                ));
    }

    @GetMapping("/api/v1/chat-rooms/{roomToken}/messages/recent")
    public ResponseEntity<ApiResponse<ChatMessageResponseDto>> getRecentChatMessages(
            @PathVariable(value = "roomToken") String token
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        "MESSAGES_LOADED_SUCCESS",
                        chatService.getRecentMessagesByRoomToken(token)
                ));
    }
}
