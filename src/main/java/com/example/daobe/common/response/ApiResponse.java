package com.example.daobe.common.response;

import lombok.Data;

@Data
public class ApiResponse<T> {

    // FIXME: 추후 응답 메시지 Enum 으로 관리하도록 구현
    private String message;
    private T data;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
