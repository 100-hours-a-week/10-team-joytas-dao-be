package com.example.daobe.lounge.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LoungeResult {
    LOUNGE_CREATED_SUCCESS(HttpStatus.CREATED),
    LOUNGE_LIST_LOADED_SUCCESS(HttpStatus.OK),
    LOUNGE_INFO_LOADED_SUCCESS(HttpStatus.OK),
    LOUNGE_DELETED_SUCCESS(HttpStatus.OK),
    LOUNGE_INVITE_SUCCESS(HttpStatus.OK),
    ;

    private final HttpStatus httpStatus;

    LoungeResult(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
