package com.example.daobe.user.application;

import com.example.daobe.user.domain.event.UserInquiriesEvent;

public interface UserExternalEventAlert {

    void execute(UserInquiriesEvent event);
}
