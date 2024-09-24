package com.example.daobe.chat.presentation;

import com.example.daobe.chat.application.ChatService;
import com.example.daobe.chat.application.dto.ChatMessageDto;
import com.example.daobe.chat.application.dto.ChatMessageResponseDto;
import com.example.daobe.chat.application.dto.ChatRoomTokenDto;
import com.example.daobe.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat-rooms")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat/greet")
    public void enterLeaveMessage(
            @AuthenticationPrincipal Long userId,
            @RequestBody ChatMessageDto messageDto
    ) {
        chatService.sendEnterLeaveMessage(userId, messageDto);
    }

    @PostMapping("/chat")
    public void sendMessage(
            @AuthenticationPrincipal Long userId,
            @RequestBody ChatMessageDto messageDto
    ) {
        chatService.sendMessage(userId, messageDto);
    }

    @GetMapping("/{objetId}/room-token")
    public ResponseEntity<ApiResponse<ChatRoomTokenDto>> getChatRoomToken(
            @PathVariable(name = "objetId") Long objetId
    ) {
        ChatRoomTokenDto roomToken = chatService.getRoomTokenByObjetId(objetId);
        ApiResponse<ChatRoomTokenDto> response = new ApiResponse<>(
                "CHAT_ROOM_TOKEN_LOADED_SUCCESS",
                roomToken
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{roomToken}/messages")
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

    @GetMapping("/{roomToken}/messages/recent")
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
