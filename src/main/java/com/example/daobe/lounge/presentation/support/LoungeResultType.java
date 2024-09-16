package com.example.daobe.lounge.presentation.support;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LoungeResultType {
    LOUNGE_CREATED_SUCCESS(HttpStatus.CREATED),
    LOUNGE_LIST_LOADED_SUCCESS(HttpStatus.OK),
    LOUNGE_INFO_LOADED_SUCCESS(HttpStatus.OK),
    LOUNGE_DELETED_SUCCESS(HttpStatus.OK),
    LOUNGE_INVITE_SUCCESS(HttpStatus.OK),
    LOUNGE_SHARER_INFO_LOADED_SUCCESS(HttpStatus.OK),
    LOUNGE_WITHDRAW_SUCCESS(HttpStatus.OK),
    ;

    private final HttpStatus httpStatus;

    LoungeResultType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
