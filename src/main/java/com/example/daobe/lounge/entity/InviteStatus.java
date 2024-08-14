package com.example.daobe.lounge.entity;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum InviteStatus {
    SUCCESS("USER_INVITED_TO_LOUNGE_SUCCESS", HttpStatus.OK),
    ALREADY_EXISTS("USER_ALREADY_EXISTS_IN_LOUNGE", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus httpStatus;

    InviteStatus(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
