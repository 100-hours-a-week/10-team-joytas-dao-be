package com.example.daobe.lounge.domain.event;

import com.example.daobe.common.domain.DomainEvent;
import com.example.daobe.lounge.domain.LoungeSharer;

public class LoungeInviteEvent implements DomainEvent {

    private static final String LOUNGE_NOT_CREATED_EXCEPTION_MESSAGE = "아직 생성되지 않은 라운지";

    private final Long domainId;
    private final Long sendUserId;
    private final Long receiveUserId;

    public LoungeInviteEvent(Long sendUserId, LoungeSharer loungeSharer) {
        validate(loungeSharer);
        this.domainId = loungeSharer.getLounge().getId();
        this.sendUserId = sendUserId;
        this.receiveUserId = loungeSharer.getUser().getId();
    }

    private void validate(LoungeSharer loungeSharer) {
        if (loungeSharer.getId() == null) {
            throw new RuntimeException(LOUNGE_NOT_CREATED_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public Long getDomainId() {
        return domainId;
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
