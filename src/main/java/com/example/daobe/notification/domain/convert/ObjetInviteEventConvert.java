package com.example.daobe.notification.domain.convert;

import com.example.daobe.notification.domain.convert.dto.DomainInfo;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.event.ObjetInviteEvent;
import com.example.daobe.objet.domain.repository.ObjetRepository;
import org.springframework.stereotype.Component;

@Component
public class ObjetInviteEventConvert implements DomainEventConvert {

    private final ObjetRepository objetRepository;

    public ObjetInviteEventConvert(ObjetRepository objetRepository) {
        this.objetRepository = objetRepository;
    }

    @Override
    public String supportNameToLowerCase() {
        return ObjetInviteEvent.class.getName().toLowerCase();
    }

    @Override
    public DomainInfo convertToDomainInfo(Long domainId) {
        Objet findObjet = objetRepository.findById(domainId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 도메인 - 404"));
        return new DomainInfo(
                findObjet.getObjetId(),
                findObjet.getName()
        );
    }
}
