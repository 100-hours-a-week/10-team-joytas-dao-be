package com.example.daobe.chat.application;

public interface ChatMessageEventPublisher {

    <T> void execute(T message);
}
