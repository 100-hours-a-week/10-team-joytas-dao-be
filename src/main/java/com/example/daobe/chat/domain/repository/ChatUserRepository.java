package com.example.daobe.chat.domain.repository;

import com.example.daobe.chat.domain.ChatUser;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatUserRepository extends MongoRepository<ChatUser, String> {

    Optional<ChatUser> findByUserId(Long userId);

    List<ChatUser> findAllByUserIdIn(Set<Long> userIds);
}
