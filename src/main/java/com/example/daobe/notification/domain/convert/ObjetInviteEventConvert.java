package com.example.daobe.notification.domain.convert;

import static com.example.daobe.objet.exception.ObjetExceptionType.INVALID_OBJET_ID_EXCEPTION;

import com.example.daobe.notification.domain.convert.dto.DomainInfo;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.event.ObjetInviteEvent;
import com.example.daobe.objet.domain.repository.ObjetRepository;
import com.example.daobe.objet.exception.ObjetException;
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
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));
        return new DomainInfo(
                findObjet.getObjetId(),
                findObjet.getName()
        );
    }
}
