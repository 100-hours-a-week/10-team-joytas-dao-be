package com.example.daobe.objet.application;

import static com.example.daobe.objet.exception.ObjetExceptionType.INVALID_OBJET_ID_EXCEPTION;
import static com.example.daobe.objet.exception.ObjetExceptionType.NO_PERMISSIONS_ON_OBJET;

import com.example.daobe.chat.domain.ChatRoom;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.objet.application.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.application.dto.ObjetDetailResponseDto;
import com.example.daobe.objet.application.dto.ObjetDetailResponseDto.SharerInfo;
import com.example.daobe.objet.application.dto.ObjetInfoResponseDto;
import com.example.daobe.objet.application.dto.ObjetMeResponseDto;
import com.example.daobe.objet.application.dto.ObjetResponseDto;
import com.example.daobe.objet.application.dto.ObjetUpdateRequestDto;
import com.example.daobe.objet.domain.Objet;
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
        Objet findObjet = getObjetById(objetId);
        validateObjetOwner(findObjet, userId);
        findObjet.updateDetailsWithImage(request.name(), request.description(), request.objetImage());
        objetRepository.save(findObjet);
        return findObjet;
    }

    public List<ObjetResponseDto> getObjetListInLoungeOfSharer(Long userId, Long loungeId) {
        return objetRepository.findActiveObjetsInLoungeOfSharer(
                userId,
                loungeId,
                ObjetStatus.ACTIVE
        );
    }

    public List<ObjetResponseDto> getObjetListInLounge(Long loungeId) {
        return objetRepository.findActiveObjetsInLounge(loungeId, ObjetStatus.ACTIVE);
    }

    public ObjetDetailResponseDto getObjetDetailInfo(Long objetId) {
        Objet findObjet = getObjetById(objetId);
        findObjet.isActiveOrThrow();

        List<SharerInfo> sharerInfos = findObjet.getObjetSharers().stream()
                .map(sharer -> SharerInfo.of(
                        sharer.getUser().getId(),
                        sharer.getUser().getNickname())
                )
                .toList();

        Long callingUserNum = objetCallRepository.getObjetLength(objetId);

        return ObjetDetailResponseDto.of(
                findObjet,
                true, // TODO : 실시간 오브제 상태 확인 로직 구현 후 변경
                callingUserNum,
                null, // TODO : 실시간 오브제 접속 유저 목록 로직 구현 후 변경
                sharerInfos
        );

    }

    public List<ObjetMeResponseDto> getMyRecentObjetList(Long userId) {
        return objetSharerRepository.findMyRecentObjetByUserId(userId, ObjetStatus.ACTIVE).stream()
                .map(ObjetMeResponseDto::of).toList();
    }

    @Transactional
    public ObjetInfoResponseDto delete(Long objetId, Long userId) {
        Objet findObjet = getObjetById(objetId);
        validateObjetOwner(findObjet, userId);

        findObjet.updateStatus(ObjetStatus.DELETED);
        objetRepository.save(findObjet);
        return ObjetInfoResponseDto.of(findObjet);
    }

    public Objet getObjetById(Long objetId) {
        return objetRepository.findById(objetId)
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));
    }

    private void validateObjetOwner(Objet findObjet, Long userId) {
        if (!findObjet.getUser().getId().equals(userId)) {
            throw new ObjetException(NO_PERMISSIONS_ON_OBJET);
        }
    }

}

