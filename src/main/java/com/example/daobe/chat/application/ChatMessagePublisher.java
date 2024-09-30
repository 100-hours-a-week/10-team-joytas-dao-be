package com.example.daobe.chat.application;

import com.example.daobe.chat.application.dto.ChatMessageInfoDto;

public interface ChatMessagePublisher {

    void execute(ChatMessageInfoDto chatMessage);
}
