package com.example.daobe.objet.application;

import com.example.daobe.lounge.domain.event.LoungeDeleteEvent;
import com.example.daobe.lounge.domain.event.LoungeWithdrawEvent;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharer;
import com.example.daobe.objet.domain.repository.ObjetRepository;
import com.example.daobe.objet.domain.repository.ObjetSharerRepository;
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
    private final ObjetSharerRepository objetSharerRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loungeDeletedListener(LoungeDeleteEvent event) {
        List<Objet> objetList = objetRepository.findActiveObjetListInLounge(event.loungeId());
        objetList.forEach(Objet::updateStatusDeleted);
        objetRepository.saveAll(objetList);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loungeWithdrawListener(LoungeWithdrawEvent event) {
        List<ObjetSharer> objetSharerList = objetSharerRepository.findByUserIdAndLoungeId(
                event.userId(),
                event.loungeId()
        );
        objetSharerList.forEach(ObjetSharer::updateStatusDeleted);
        objetSharerRepository.saveAll(objetSharerList);
    }
}
