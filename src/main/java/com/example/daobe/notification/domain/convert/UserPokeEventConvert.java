package com.example.daobe.notification.domain.convert;

import com.example.daobe.notification.domain.convert.dto.DomainInfo;
import com.example.daobe.user.domain.event.UserPokeEvent;
import org.springframework.stereotype.Component;

@Component
public class UserPokeEventConvert implements DomainEventConvert {

    @Override
    public String supportNameToLowerCase() {
        return UserPokeEvent.class.getName().toLowerCase();
    }

    @Override
    public DomainInfo convertToDomainInfo(Long domainId) {
        return null;
    }
}
