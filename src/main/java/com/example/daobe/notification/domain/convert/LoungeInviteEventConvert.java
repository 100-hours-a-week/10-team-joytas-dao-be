package com.example.daobe.notification.domain.convert;

import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.event.LoungeInviteEvent;
import com.example.daobe.lounge.domain.repository.LoungeRepository;
import com.example.daobe.notification.domain.convert.dto.DomainInfo;
import org.springframework.stereotype.Component;

@Component
public class LoungeInviteEventConvert implements DomainEventConvert {

    private final LoungeRepository loungeRepository;

    public LoungeInviteEventConvert(LoungeRepository loungeRepository) {
        this.loungeRepository = loungeRepository;
    }

    @Override
    public String supportNameToLowerCase() {
        return LoungeInviteEvent.class.getName().toLowerCase();
    }

    @Override
    public DomainInfo convertToDomainInfo(Long domainId) {
        Lounge findLounge = loungeRepository.findById(domainId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 도메인 - 404"));
        return new DomainInfo(
                findLounge.getId(),
                findLounge.getName()
        );
    }
}
