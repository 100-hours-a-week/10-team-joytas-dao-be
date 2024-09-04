package com.example.daobe.objet.application;

import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_ID_EXCEPTION;
import static com.example.daobe.objet.exception.ObjetExceptionType.INVALID_OBJET_ID_EXCEPTION;
import static com.example.daobe.objet.exception.ObjetExceptionType.NO_PERMISSIONS_ON_OBJET;
import static com.example.daobe.user.exception.UserExceptionType.NOT_EXIST_USER;

import com.example.daobe.chat.application.ChatService;
import com.example.daobe.chat.domain.ChatRoom;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.repository.LoungeRepository;
import com.example.daobe.lounge.exception.LoungeException;
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
import com.example.daobe.objet.domain.event.ObjetInviteEvent;
import com.example.daobe.objet.domain.repository.ObjetCallRepository;
import com.example.daobe.objet.domain.repository.ObjetRepository;
import com.example.daobe.objet.domain.repository.ObjetSharerRepository;
import com.example.daobe.objet.exception.ObjetException;
import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.repository.UserRepository;
import com.example.daobe.user.exception.UserException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ObjetService {

    private final ObjetRepository objetRepository;
    private final LoungeRepository loungeRepository;
    private final UserRepository userRepository;
    private final ObjetSharerRepository objetSharerRepository;
    private final ChatService chatRoomService;
    private final ObjetCallRepository objetCallRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ObjetCreateResponseDto create(Long userId, ObjetCreateRequestDto request) {
        Lounge lounge = getLoungeById(request.loungeId());
        User creator = getUserById(userId);
        ChatRoom chatRoom = chatRoomService.createChatRoom();

        Objet objet = Objet.builder()
                .name(request.name())
                .explanation(request.description())
                .type(ObjetType.from(request.type()))
                .status(ObjetStatus.ACTIVE)
                .user(creator)
                .lounge(lounge)
                .imageUrl(request.objetImage())
                .chatRoom(chatRoom)
                .build();
        objetRepository.save(objet);

        List<Long> sharerIds = request.sharers();
        sharerIds.add(userId);

        List<ObjetSharer> objetSharers = manageSharers(objet, sharerIds, userId);
        objet.updateUserObjets(objetSharers);

        return ObjetCreateResponseDto.of(objet);
    }

    @Transactional
    public ObjetCreateResponseDto update(Long userId, Long objetId, ObjetUpdateRequestDto request) {

        Objet findObjet = getObjetById(objetId);
        validateObjetOwner(findObjet, userId);

        if (request.objetImage() == null) {
            findObjet.updateDetails(request.name(), request.description());
        } else {
            findObjet.updateDetailsWithImage(request.name(), request.description(), request.objetImage());
        }

        Set<Long> currentSharerIds = getCurrentSharerIds(findObjet);
        List<Long> newSharerIds = request.sharers();
        manageAndSyncSharers(findObjet, currentSharerIds, newSharerIds, userId);

        objetRepository.save(findObjet);
        return ObjetCreateResponseDto.of(findObjet);
    }

    public List<ObjetInfoDto> getObjetList(Long userId, Long loungeId, Boolean sharer) {
        if (Boolean.TRUE.equals(sharer)) {
            return objetRepository.findByLoungeIdAndDeletedAtIsNullAndStatusAndObjetSharersUserId(
                            loungeId,
                            ObjetStatus.ACTIVE,
                            userId
                    )
                    .stream()
                    .map(ObjetInfoDto::of)
                    .toList();
        } else {
            return objetRepository.findByLoungeIdAndDeletedAtIsNullAndStatus(loungeId, ObjetStatus.ACTIVE)
                    .stream()
                    .map(ObjetInfoDto::of)
                    .toList();
        }
    }

    public ObjetDetailInfoDto getObjetDetail(Long objetId) {
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

    public List<ObjetMeInfoDto> getMyRecentObjets(Long userId) {
        return objetSharerRepository.findByUserId(userId).stream()
                .map(ObjetSharer::getObjet)
                .filter(objet -> objet.getStatus() == ObjetStatus.ACTIVE && objet.getDeletedAt() == null)
                .sorted(Comparator.comparing(Objet::getCreatedAt).reversed())
                .limit(4)
                .map(ObjetMeInfoDto::of)
                .toList();
    }

    @Transactional
    public ObjetCreateResponseDto delete(Long objetId, Long userId) {
        Objet findObjet = getObjetById(objetId);
        validateObjetOwner(findObjet, userId);

        findObjet.updateStatus(ObjetStatus.DELETED);
        objetRepository.save(findObjet);
        return ObjetCreateResponseDto.of(findObjet);
    }

    private void validateObjetOwner(Objet findObjet, Long userId) {
        if (!findObjet.getUser().getId().equals(userId)) {
            throw new ObjetException(NO_PERMISSIONS_ON_OBJET);
        }
    }

    private Lounge getLoungeById(Long loungeId) {
        return loungeRepository.findById(loungeId)
                .orElseThrow(() -> new LoungeException(INVALID_LOUNGE_ID_EXCEPTION));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
    }

    private Objet getObjetById(Long objetId) {
        return objetRepository.findById(objetId)
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));
    }

    private Set<Long> getCurrentSharerIds(Objet findObjet) {
        return findObjet.getObjetSharers().stream()
                .map(objetSharer -> objetSharer.getUser().getId())
                .collect(Collectors.toSet());
    }

    private List<ObjetSharer> manageSharers(Objet objet, List<Long> sharerIds, Long userId) {
        return sharerIds.stream()
                .map(sharerId -> {
                    User user = getUserById(sharerId);
                    ObjetSharer newObjetSharer = ObjetSharer.builder()
                            .user(user)
                            .objet(objet)
                            .build();
                    objetSharerRepository.save(newObjetSharer);
                    eventPublisher.publishEvent(new ObjetInviteEvent(userId, newObjetSharer));
                    return newObjetSharer;
                })
                .toList();
    }

    private void manageAndSyncSharers(
            Objet findObjet,
            Set<Long> currentSharerIds,
            List<Long> newSharerIds,
            Long userId
    ) {
        for (Long newSharerId : newSharerIds) {
            if (!currentSharerIds.contains(newSharerId)) {
                User user = getUserById(newSharerId);
                ObjetSharer newObjetSharer = ObjetSharer.builder()
                        .user(user)
                        .objet(findObjet)
                        .build();
                objetSharerRepository.save(newObjetSharer);
                eventPublisher.publishEvent(new ObjetInviteEvent(userId, newObjetSharer));
                findObjet.getObjetSharers().add(newObjetSharer);
            }
        }

        findObjet.getObjetSharers().removeIf(objetSharer -> {
            if (!newSharerIds.contains(objetSharer.getUser().getId())) {
                objetSharerRepository.delete(objetSharer);
                return true;
            }
            return false;
        });
    }
}

