package com.example.daobe.lounge.domain.event;

public record LoungeInviteAcceptedEvent(
        Long invitedUserId,
        Long loungeId
) {
}
