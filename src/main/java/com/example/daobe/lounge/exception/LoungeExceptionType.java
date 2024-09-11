package com.example.daobe.lounge.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum LoungeExceptionType implements BaseExceptionType {
    INVALID_LOUNGE_ID_EXCEPTION("유효하지 않은 라운지 ID입니다.", HttpStatus.NOT_FOUND),
    INVALID_LOUNGE_TYPE_EXCEPTION("유효하지 않은 라운지 타입입니다.", HttpStatus.BAD_REQUEST),
    NOT_ACTIVE_LOUNGE_EXCEPTION("활성 상태가 아닌 라운지입니다.", HttpStatus.NOT_FOUND),
    INVALID_LOUNGE_OWNER_EXCEPTION("유효하지 않은 라운지 주인입니다.", HttpStatus.FORBIDDEN),
    INVALID_LOUNGE_SHARER_EXCEPTION("유효하지 않은 라운지 공유자입니다.", HttpStatus.FORBIDDEN),
    ALREADY_INVITED_USER_EXCEPTION("이미 라운지에 소속된 유저입니다.", HttpStatus.FORBIDDEN),
    ALREADY_DELETED_LOUNGE_EXCEPTION("이미 삭제된 라운지입니다.", HttpStatus.FORBIDDEN),
    MAXIMUM_LOUNGE_LIMIT_EXCEEDED_EXCEPTION("라운지 개수는 최대 4개를 초과할 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_ALLOW_LOUNGE_WITHDRAW_EXCEPTION("라운지 생성자는 탈퇴할 수 없습니다.", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus status;

    LoungeExceptionType(String message, HttpStatus status) {
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
