package com.example.daobe.chat.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum ChatExceptionType implements BaseExceptionType {

    SOCKET_CONNECTION_FAILED_EXCEPTION("소켓 연결에 실패했습니다.", HttpStatus.SERVICE_UNAVAILABLE),
    INVALID_CHAT_ROOM_ID_EXCEPTION("유효하지 않은 채팅방 ID입니다.", HttpStatus.NOT_FOUND),
    ;

    private final String message;
    private final HttpStatus status;

    ChatExceptionType(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus status() {
        return status;
    }
}
