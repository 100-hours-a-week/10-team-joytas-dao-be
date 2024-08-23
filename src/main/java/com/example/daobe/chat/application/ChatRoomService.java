package com.example.daobe.chat.application;

import static com.example.daobe.chat.exception.ChatRoomExceptionType.INVALID_CHAT_ROOM_ID_EXCEPTION;

import com.example.daobe.chat.domain.ChatRoom;
import com.example.daobe.chat.domain.repository.ChatRoomRepository;
import com.example.daobe.chat.exception.ChatRoomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom createChatRoom() {
        return chatRoomRepository.save(new ChatRoom());
    }

    public ChatRoom findChatRoomById(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new ChatRoomException(INVALID_CHAT_ROOM_ID_EXCEPTION));
    }
}
