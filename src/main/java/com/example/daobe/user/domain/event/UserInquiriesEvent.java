package com.example.daobe.user.domain.event;

public record UserInquiriesEvent(
        Long userId,
        String nickname,
        String email,
        String contents
) {
}
