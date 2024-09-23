package com.example.daobe.objet.application;

import com.example.daobe.lounge.domain.event.LoungeDeleteEvent;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetStatus;
import com.example.daobe.objet.domain.repository.ObjetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ObjetEventListener {

    private final ObjetRepository objetRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleLoungeDeleted(LoungeDeleteEvent event) {
        List<Objet> objetList = objetRepository.findActiveObjetListInLounge(event.loungeId());
        objetList.forEach(objet -> objet.updateStatus(ObjetStatus.DELETED));
        objetRepository.saveAll(objetList);
    }
}
