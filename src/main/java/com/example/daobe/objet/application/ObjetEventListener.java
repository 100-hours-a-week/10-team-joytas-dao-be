package com.example.daobe.objet.application;

import com.example.daobe.lounge.domain.event.LoungeDeleteEvent;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetStatus;
import com.example.daobe.objet.domain.repository.ObjetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ObjetEventListener {

    private final ObjetRepository objetRepository;

    @EventListener
    public void handleLoungeDeleted(LoungeDeleteEvent event) {
        List<Objet> objets = objetRepository.findActiveObjetsInLounge(event.getDomainId(), ObjetStatus.ACTIVE);
        objets.forEach(objet -> objet.updateStatus(ObjetStatus.DELETED));
        objetRepository.saveAll(objets);
    }
}
