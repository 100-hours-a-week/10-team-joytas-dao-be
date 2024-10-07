package com.example.daobe.lounge.domain.event;

import static com.example.daobe.lounge.exception.LoungeExceptionType.LOUNGE_NOT_CREATED_EXCEPTION_MESSAGE;

import com.example.daobe.common.domain.DomainEvent;
import com.example.daobe.lounge.domain.LoungeSharer;
import com.example.daobe.lounge.exception.LoungeException;

public class LoungeInvitedEvent implements DomainEvent {

    private final Long domainId;
    private final Long sendUserId;
    private final Long receiveUserId;

    public LoungeInvitedEvent(Long sendUserId, LoungeSharer loungeSharer) {
        validate(loungeSharer);
        this.domainId = loungeSharer.getLounge().getId();
        this.sendUserId = sendUserId;
        this.receiveUserId = loungeSharer.getUser().getId();
    }

    private void validate(LoungeSharer loungeSharer) {
        if (loungeSharer.getId() == null) {
            throw new LoungeException(LOUNGE_NOT_CREATED_EXCEPTION_MESSAGE);
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
