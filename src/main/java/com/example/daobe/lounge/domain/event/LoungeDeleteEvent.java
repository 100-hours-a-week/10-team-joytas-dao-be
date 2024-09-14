package com.example.daobe.lounge.domain.event;

public class LoungeDeleteEvent {

    private final Long domainId;

    public LoungeDeleteEvent(Long loungeId) {
        this.domainId = loungeId;
    }

    public Long getDomainId() {
        return domainId;
    }


}
