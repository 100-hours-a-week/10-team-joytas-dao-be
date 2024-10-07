package com.example.daobe.lounge.domain.event;

public record LoungeWithdrawEvent(
        Long userId,
        Long loungeId
) {
}
