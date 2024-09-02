package com.example.daobe.upload.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum UploadExceptionType implements BaseExceptionType {
    FILE_ENCODE_FAIL("파일 인코딩 실패", HttpStatus.BAD_REQUEST),
    UPLOAD_IMAGE_FAIL("이미지 업로드 실패", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus status;

    UploadExceptionType(String message, HttpStatus status) {
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
