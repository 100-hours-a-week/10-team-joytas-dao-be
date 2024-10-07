package com.example.daobe.user.domain.event;

import com.example.daobe.common.domain.DomainEvent;
import java.util.UUID;

public class UserPokeEvent implements DomainEvent {

    private final String eventId;
    private final Long sendUserId;
    private final Long receiveUserId;

    public UserPokeEvent(Long sendUserId, Long receiveUserId) {
        this.eventId = UUID.randomUUID().toString();
        this.sendUserId = sendUserId;
        this.receiveUserId = receiveUserId;
    }

    @Override
    public String getEventId() {
        return eventId;
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
