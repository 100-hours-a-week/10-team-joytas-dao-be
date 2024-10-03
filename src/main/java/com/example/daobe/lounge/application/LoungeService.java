package com.example.daobe.lounge.application;

import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_ID_EXCEPTION;
import static com.example.daobe.objet.domain.ObjetStatus.ACTIVE;

import com.example.daobe.lounge.application.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.application.dto.LoungeDetailInfoDto;
import com.example.daobe.lounge.application.dto.LoungeDetailInfoDto.ObjetInfo;
import com.example.daobe.lounge.application.dto.LoungeInfoDto;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.LoungeStatus;
import com.example.daobe.lounge.domain.LoungeType;
import com.example.daobe.lounge.domain.event.LoungeDeletedEvent;
import com.example.daobe.lounge.domain.repository.LoungeRepository;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoungeService {

    private final LoungeRepository loungeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Lounge createAndSaveLounge(LoungeCreateRequestDto request, User user) {
        Lounge lounge = Lounge.builder()
                .user(user)
                .name(request.name())
                .type(LoungeType.from(request.type()))
                .status(LoungeStatus.ACTIVE)
                .build();
        loungeRepository.save(lounge);
        return lounge;
    }

    public List<LoungeInfoDto> getLoungeInfosByUserId(Long userId) {
        return loungeRepository.findLoungeByUserId(userId).stream()
                .filter(lounge -> lounge.getStatus().isActive())
                .map(LoungeInfoDto::of)
                .toList();
    }

    public LoungeDetailInfoDto getLoungeDetailInfo(Lounge lounge) {
        lounge.isActiveOrThrow();
        List<ObjetInfo> objetInfos = lounge.getObjets()
                .stream()
                .filter(objet -> objet.getStatus() == ACTIVE)
                .map(ObjetInfo::of)
                .toList();
        return LoungeDetailInfoDto.of(lounge, objetInfos);
    }

    public Lounge getLoungeById(Long loungeId) {
        return loungeRepository.findById(loungeId)
                .orElseThrow(() -> new LoungeException(INVALID_LOUNGE_ID_EXCEPTION));
    }

    public void deleteLoungeByUserId(User user, Lounge lounge) {
        lounge.softDelete(user.getId());
        eventPublisher.publishEvent(new LoungeDeletedEvent(lounge.getId()));
    }
}
