package com.example.daobe.user.application;

import com.example.daobe.user.domain.event.UserInquiriesEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class UserExternalEventListener {

    private final UserExternalEventAlert userExternalEventAlert;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listenInquiriesEvent(UserInquiriesEvent event) {
        userExternalEventAlert.execute(event);
    }
}
