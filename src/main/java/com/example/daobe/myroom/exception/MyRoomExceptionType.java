package com.example.daobe.myroom.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum MyRoomExceptionType implements BaseExceptionType {
    NOT_EXIST_MY_ROOM("존재하지 않는 마이룸 입니다.", HttpStatus.NOT_FOUND),
    ALREADY_EXIST_MY_ROOM("회원당 마이룸은 한개만 생성할 수 있습니다.", HttpStatus.CONFLICT),
    FORBIDDEN_MY_ROOM_MODIFICATION("다른 사람의 마이룸은 변경할 수 없습니다.", HttpStatus.FORBIDDEN),
    ;

    private final String message;
    private final HttpStatus status;

    MyRoomExceptionType(String message, HttpStatus status) {
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
