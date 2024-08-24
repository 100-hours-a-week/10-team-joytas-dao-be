package com.example.daobe.chat.presentation;

import com.example.daobe.chat.application.ChatService;
import com.example.daobe.chat.application.dto.ChatMessageDto;
import com.example.daobe.chat.application.dto.ChatRoomTokenDto;
import com.example.daobe.common.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            @Payload ChatMessageDto message,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        ChatMessageDto.EnterMessage newMessage = chatService.createEnterMessageAndSetSessionAttribute(
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
        messagingTemplate.convertAndSend(
                SUBSCRIBE_URL.formatted(token),
                chatService.createMessage(message, token)
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

    /**
     * ?all=true : 전체 메시지 ?all=false : 최근 3개 메시지
     */
    @GetMapping("/api/v1/chat-rooms/{roomToken}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageDto>>> getChatMessages(
            @PathVariable(name = "roomToken") String token,
            @RequestParam(name = "all") boolean isAll
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        "MESSAGES_LOADED_SUCCESS",
                        chatService.getMessagesByRoomToken(token, isAll)
                ));
    }
}
