package com.example.daobe.chat.domain;

public enum MessageType {
    ENTER("%s 님이 입장하셨습니다."),
    TALK(""),
    LEAVE("%s 님이 퇴장하셨습니다."),
    ;

    private final String message;

    MessageType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
