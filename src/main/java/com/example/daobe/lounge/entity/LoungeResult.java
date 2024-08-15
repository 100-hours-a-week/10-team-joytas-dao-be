package com.example.daobe.lounge.entity;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LoungeResult {
    LOUNGE_CREATED_SUCCESS("LOUNGE_CREATED_SUCCESS", HttpStatus.OK),
    LOUNGE_LIST_LOADED_SUCCESS("LOUNGE_LIST_LOADED_SUCCESS", HttpStatus.OK),
    LOUNGE_INFO_LOADED_SUCCESS("LOUNGE_INFO_LOADED_SUCCESS", HttpStatus.OK),
    LOUNGE_DELETED_SUCCESS("LOUNGE_DELETED_SUCCESS", HttpStatus.OK),
    LOUNGE_INVITE_SUCCESS("USER_INVITED_TO_LOUNGE_SUCCESS", HttpStatus.OK),
    LOUNGE_ALREADY_EXISTS_USER("USER_ALREADY_EXISTS_IN_LOUNGE", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus httpStatus;

    LoungeResult(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
