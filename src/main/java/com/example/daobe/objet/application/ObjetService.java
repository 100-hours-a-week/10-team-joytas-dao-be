package com.example.daobe.objet.application;

import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_SHARER_EXCEPTION;
import static com.example.daobe.objet.exception.ObjetExceptionType.INVALID_OBJET_ID_EXCEPTION;
import static com.example.daobe.objet.exception.ObjetExceptionType.NO_PERMISSIONS_ON_OBJET;

import com.example.daobe.chat.domain.ChatRoom;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.repository.LoungeSharerRepository;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.objet.application.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.application.dto.ObjetDetailResponseDto;
import com.example.daobe.objet.application.dto.ObjetInfoResponseDto;
import com.example.daobe.objet.application.dto.ObjetResponseDto;
import com.example.daobe.objet.application.dto.ObjetUpdateRequestDto;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharer;
import com.example.daobe.objet.domain.ObjetStatus;
import com.example.daobe.objet.domain.ObjetType;
import com.example.daobe.objet.domain.repository.ObjetCallRepository;
import com.example.daobe.objet.domain.repository.ObjetRepository;
import com.example.daobe.objet.domain.repository.ObjetSharerRepository;
import com.example.daobe.objet.exception.ObjetException;
import com.example.daobe.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObjetService {

    private final ObjetRepository objetRepository;
    private final ObjetSharerRepository objetSharerRepository;
    private final ObjetCallRepository objetCallRepository;
    private final LoungeSharerRepository loungeSharerRepository;

    @Transactional
    public Objet createAndSaveObjet(
            ObjetCreateRequestDto request,
            User user,
            Lounge lounge,
            ChatRoom chatRoom
    ) {
        Objet objet = Objet.builder()
                .name(request.name())
                .explanation(request.description())
                .type(ObjetType.from(request.type()))
                .user(user)
                .lounge(lounge)
                .imageUrl(request.objetImage())
                .chatRoom(chatRoom)
                .build();
        objetRepository.save(objet);

        return objet;
    }

    @Transactional
    public Objet updateAndSaveObjet(ObjetUpdateRequestDto request, Long objetId, Long userId) {
        Objet findObjet = objetRepository.findByIdAndStatus(objetId, ObjetStatus.ACTIVE)
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));
        validateObjetOwner(findObjet, userId);
        findObjet.updateDetailsWithImage(request.name(), request.description(), request.objetImage());
        return objetRepository.save(findObjet);
    }

    public List<ObjetResponseDto> getObjetListInLoungeOfSharer(Long userId, Long loungeId) {
        return objetRepository.findActiveObjetsInLoungeOfSharer(
                userId,
                loungeId,
                ObjetStatus.ACTIVE
        );
    }

    public List<ObjetResponseDto> getObjetListInLounge(Long loungeId) {
        List<Objet> objetList = objetRepository.findActiveObjetsInLounge(loungeId, ObjetStatus.ACTIVE);
        return ObjetResponseDto.listOf(objetList);
    }

    public ObjetDetailResponseDto getObjetDetailInfo(Long userId, Long objetId) {

        Objet findObjet = objetRepository.findById(objetId)
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));

        List<ObjetSharer> objetSharerList = objetSharerRepository.findAllByObjetId(objetId);

        boolean isLoungeSharer = loungeSharerRepository.existsByUserIdAndLoungeId(userId,
                findObjet.getLounge().getId());
        if (!isLoungeSharer) {
            throw new LoungeException(INVALID_LOUNGE_SHARER_EXCEPTION);
        }

        Long callingUserNum = objetCallRepository.getObjetLength(objetId);

        return ObjetDetailResponseDto.of(
                findObjet,
                callingUserNum,
                objetSharerList
        );
    }


    @Transactional
    public ObjetInfoResponseDto delete(Long objetId, Long userId) {
        Objet findObjet = getObjetById(objetId);
        validateObjetOwner(findObjet, userId);

        findObjet.updateStatus(ObjetStatus.DELETED);
        objetRepository.save(findObjet);
        return ObjetInfoResponseDto.of(findObjet);
    }

    private Objet getObjetById(Long objetId) {
        return objetRepository.findByIdAndStatus(objetId, ObjetStatus.ACTIVE)
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));
    }

    private void validateObjetOwner(Objet findObjet, Long userId) {
        if (!findObjet.getUser().getId().equals(userId)) {
            throw new ObjetException(NO_PERMISSIONS_ON_OBJET);
        }
    }

}
