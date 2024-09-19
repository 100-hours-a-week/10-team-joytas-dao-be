package com.example.daobe.chat.application.event;

import com.example.daobe.chat.domain.ChatUser;
import com.example.daobe.chat.domain.repository.ChatUserRepository;
import com.example.daobe.user.domain.event.UserCreateEvent;
import com.example.daobe.user.domain.event.UserUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatUserEventListener {

    private final ChatUserRepository chatUserRepository;

    @EventListener
    public void handleUserCreated(UserCreateEvent event) {
        ChatUser chatUser = createUserIfNotExistsOrUpdate(event);
        chatUserRepository.save(chatUser);
    }

    @EventListener
    public void handleUserUpdated(UserUpdateEvent event) {
        ChatUser findUser = chatUserRepository.findByUserId(event.userId())
                .orElseThrow(() -> new RuntimeException("NOT_EXISTS_USER_EXCEPTION"));
        findUser.updateUserInfo(event.nickname(), event.profileUrl());
        chatUserRepository.save(findUser);
    }

    private ChatUser createUserIfNotExistsOrUpdate(UserCreateEvent event) {
        return chatUserRepository.findByUserId(event.userId())
                .map(user -> {
                    user.updateUserInfo(event.nickname(), event.profileUrl());
                    return user;
                })
                .orElseGet(() -> ChatUser.builder()
                        .userId(event.userId())
                        .nickname(event.nickname())
                        .profileUrl(event.profileUrl())
                        .build());
    }
}
