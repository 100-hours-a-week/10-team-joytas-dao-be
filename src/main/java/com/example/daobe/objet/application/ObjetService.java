package com.example.daobe.objet.application;

import static com.example.daobe.objet.exception.ObjetExceptionType.INVALID_OBJET_ID_EXCEPTION;
import static com.example.daobe.objet.exception.ObjetExceptionType.NO_PERMISSIONS_ON_OBJET;

import com.example.daobe.chat.domain.ChatRoom;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.objet.application.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.application.dto.ObjetCreateResponseDto;
import com.example.daobe.objet.application.dto.ObjetDetailInfoDto;
import com.example.daobe.objet.application.dto.ObjetDetailInfoDto.SharerInfo;
import com.example.daobe.objet.application.dto.ObjetInfoDto;
import com.example.daobe.objet.application.dto.ObjetMeInfoDto;
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
import java.util.Comparator;
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
    public Objet updateAndSaveObjet(ObjetUpdateRequestDto request, Objet objet, Long userId) {
        validateObjetOwner(objet, userId);
        objet.updateDetailsWithImage(request.name(), request.description(), request.objetImage());
        objetRepository.save(objet);
        return objet;
    }

    public List<ObjetInfoDto> getObjetListInLoungeOfSharer(Long userId, Long loungeId, Boolean sharer) {
        return objetRepository.findByLoungeIdAndDeletedAtIsNullAndStatusAndObjetSharersUserId(
                        loungeId,
                        ObjetStatus.ACTIVE,
                        userId
                )
                .stream()
                .map(ObjetInfoDto::of)
                .toList();
    }

    public List<ObjetInfoDto> getObjetListInLounge(Long loungeId) {
        return objetRepository.findByLoungeIdAndDeletedAtIsNullAndStatus(loungeId, ObjetStatus.ACTIVE)
                .stream()
                .map(ObjetInfoDto::of)
                .toList();
    }

    public ObjetDetailInfoDto getObjetDetailInfo(Objet objet) {
        Long objetId = objet.getObjetId();
        Objet findObjet = getObjetById(objetId);
        findObjet.isActiveOrThrow();

        List<SharerInfo> sharerInfos = findObjet.getObjetSharers().stream()
                .map(sharer -> SharerInfo.of(
                        sharer.getUser().getId(),
                        sharer.getUser().getNickname())
                )
                .toList();

        Long callingUserNum = objetCallRepository.getObjetLength(objetId);

        return ObjetDetailInfoDto.builder()
                .objetId(objetId)
                .name(findObjet.getName())
                .userId(findObjet.getUser().getId())
                .nickname(findObjet.getUser().getNickname())
                .loungeId(findObjet.getLounge().getId())
                .objetImage(findObjet.getImageUrl())
                .description(findObjet.getExplanation())
                .type(findObjet.getType())
                // TODO : 실시간 오브제 상태 확인 로직 구현 후 변경
                .isActive(true)
                // TODO : 실시간 오브제 접속 유저 목록 로직 구현 후 변경
                .viewers(null)
                .callingUserNum(callingUserNum)
                .sharers(sharerInfos)
                .build();
    }

    public List<ObjetMeInfoDto> getMyRecentObjetList(Long userId) {
        return objetSharerRepository.findByUserId(userId).stream()
                .map(ObjetSharer::getObjet)
                .filter(objet -> objet.getStatus() == ObjetStatus.ACTIVE && objet.getDeletedAt() == null)
                .sorted(Comparator.comparing(Objet::getCreatedAt).reversed())
                .limit(4)
                .map(ObjetMeInfoDto::of)
                .toList();
    }

    @Transactional
    public ObjetCreateResponseDto delete(Objet findObjet, Long userId) {
        validateObjetOwner(findObjet, userId);

        findObjet.updateStatus(ObjetStatus.DELETED);
        objetRepository.save(findObjet);
        return ObjetCreateResponseDto.of(findObjet);
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

