package com.example.daobe.user.domain.event;

import com.example.daobe.common.domain.DomainEvent;

public class UserPokeEvent implements DomainEvent {

    private final Long sendUserId;
    private final Long receiveUserId;

    public UserPokeEvent(Long sendUserId, Long receiveUserId) {
        this.sendUserId = sendUserId;
        this.receiveUserId = receiveUserId;
    }

    @Override
    public Long getDomainId() {
        return null;
    }

    @Override
    public Long getSendUserId() {
        return sendUserId;
    }

    @Override
    public Long getReceiveUserId() {
        return receiveUserId;
    }
}
