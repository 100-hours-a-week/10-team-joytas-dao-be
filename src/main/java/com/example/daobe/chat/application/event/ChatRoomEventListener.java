package com.example.daobe.chat.application.event;

import com.example.daobe.chat.domain.ChatRoom;
import com.example.daobe.chat.domain.repository.ChatRoomRepository;
import com.example.daobe.objet.domain.event.ObjetCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomEventListener {

    private final ChatRoomRepository chatRoomRepository;

    @EventListener
    public void handleChatRoomCreated(ObjetCreateEvent event) {
        log.info("[Create ChatRoom!] - objet {}", event.objet().getId());
        chatRoomRepository.save(new ChatRoom(event.objet()));
    }
}
