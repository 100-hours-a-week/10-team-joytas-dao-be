package com.example.daobe.chat.domain.repository;

import com.example.daobe.chat.domain.ChatMessage;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findAllByRoomToken(String roomToken);

    List<ChatMessage> findAllByRoomTokenOrderByCreatedAtDesc(String roomToken);
}
