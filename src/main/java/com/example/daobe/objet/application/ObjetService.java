package com.example.daobe.objet.application;

import static com.example.daobe.objet.exception.ObjetExceptionType.INVALID_OBJET_ID_EXCEPTION;

import com.example.daobe.chat.application.ChatService;
import com.example.daobe.lounge.application.LoungeService;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.objet.application.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.application.dto.ObjetCreateResponseDto;
import com.example.daobe.objet.application.dto.ObjetDetailResponseDto;
import com.example.daobe.objet.application.dto.ObjetMeResponseDto;
import com.example.daobe.objet.application.dto.ObjetResponseDto;
import com.example.daobe.objet.application.dto.ObjetUpdateRequestDto;
import com.example.daobe.objet.application.dto.ObjetUpdateResponseDto;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharer;
import com.example.daobe.objet.domain.ObjetStatus;
import com.example.daobe.objet.domain.ObjetType;
import com.example.daobe.objet.domain.event.ObjetCreateEvent;
import com.example.daobe.objet.domain.event.ObjetDeleteEvent;
import com.example.daobe.objet.domain.repository.ObjetRepository;
import com.example.daobe.objet.exception.ObjetException;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ObjetService {

    private final ApplicationEventPublisher eventPublisher;
    private final ObjetSharerService objetSharerService;
    private final ObjetRepository objetRepository;
    private final LoungeService loungeService;
    private final UserService userService;
    private final ChatService chatService;

    @Transactional
    public ObjetCreateResponseDto createNewObjet(ObjetCreateRequestDto request, Long userId) {
        Lounge findLounge = loungeService.getLoungeById(request.loungeId());
        User findUser = userService.getUserById(userId);

        Objet newObjet = Objet.builder()
                .name(request.name())
                .explanation(request.description())
                .type(ObjetType.from(request.type()))
                .user(findUser)
                .lounge(findLounge)
                .imageUrl(request.objetImage())
                .build();
        objetRepository.save(newObjet);
        objetSharerService.createAndSaveObjetSharer(request, userId, newObjet);

        eventPublisher.publishEvent(new ObjetCreateEvent(newObjet.getId()));
        return ObjetCreateResponseDto.of(newObjet);
    }

    @Transactional
    public ObjetUpdateResponseDto updateObjet(ObjetUpdateRequestDto request, Long objetId, Long userId) {
        Objet findObjet = objetRepository.findByIdAndStatus(objetId, ObjetStatus.ACTIVE)
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));
        findObjet.isOwnerOrThrow(userId);
        findObjet.updateDetailsWithImage(request.name(), request.description(), request.objetImage());

        Objet updatedObjet = objetRepository.save(findObjet);
        objetSharerService.updateObjetSharerList(updatedObjet, request.sharers());
        return ObjetUpdateResponseDto.of(updatedObjet);
    }

    public List<ObjetResponseDto> getAllObjetsInLounge(Long userId, Long loungeId, boolean isOwner) {
        if (isOwner) {
            return ObjetResponseDto.listOf(objetRepository.findActiveObjetListInLoungeOfSharer(
                    userId, loungeId
            ));
        }
        return ObjetResponseDto.listOf(
                objetRepository.findActiveObjetListInLounge(loungeId)
        );
    }

    public ObjetDetailResponseDto getObjetDetail(Long userId, Long objetId) {
        Objet findObjet = getObjetById(objetId);
        return ObjetDetailResponseDto.of(findObjet);
    }

    public List<ObjetMeResponseDto> getMyObjetList(Long userId) {
        List<ObjetSharer> objetSharerList = objetSharerService.getRecentObjetSharerList(userId);
        return objetSharerList.stream()
                .filter(objetSharer -> objetSharer.getObjet().getStatus() == ObjetStatus.ACTIVE)
                .map((objetSharer) -> ObjetMeResponseDto.of(objetSharer.getObjet()))
                .toList();
    }

    @Transactional
    public void deleteObjet(Long objetId, Long userId) {
        Objet findObjet = getObjetById(objetId);
        findObjet.isOwnerOrThrow(userId);
        findObjet.updateStatus(ObjetStatus.DELETED);
        objetRepository.save(findObjet);
        eventPublisher.publishEvent(new ObjetDeleteEvent(findObjet.getId()));
    }


    private Objet getObjetById(Long objetId) {
        return objetRepository.findByIdAndStatus(objetId, ObjetStatus.ACTIVE)
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));
    }

}
