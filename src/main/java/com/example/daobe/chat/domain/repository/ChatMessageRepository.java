package com.example.daobe.chat.domain.repository;

import com.example.daobe.chat.domain.ChatMessage;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, ObjectId> {

    List<ChatMessage> findAllByRoomTokenOrderByCreatedAtDesc(String roomToken);

    Slice<ChatMessage> findByRoomToken(String roomToken, Pageable pageable);

    // 과거 메시지 (cursorId 이전 메시지)
    Slice<ChatMessage> findByRoomTokenAndIdLessThan(String roomToken, ObjectId cursorId, Pageable pageable
    );
}
