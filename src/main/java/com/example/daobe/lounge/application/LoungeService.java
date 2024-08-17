package com.example.daobe.lounge.application;

import com.example.daobe.lounge.application.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.application.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.application.dto.LoungeDetailInfoDto;
import com.example.daobe.lounge.application.dto.LoungeInfoDto;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.LoungeStatus;
import com.example.daobe.lounge.domain.LoungeType;
import com.example.daobe.lounge.domain.repository.LoungeRepository;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.lounge.exception.LoungeExceptionType;
import com.example.daobe.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoungeService {

    private final LoungeRepository loungeRepository;

    @Transactional
    public LoungeCreateResponseDto createLounge(LoungeCreateRequestDto request, User user) {
        Lounge lounge = Lounge.builder()
                .user(user)
                .name(request.name())
                .type(LoungeType.from(request.type()))  // 라운지 타입
                .status(LoungeStatus.ACTIVE)    // 라운지 활성화
                .build();

        loungeRepository.save(lounge);
        return LoungeCreateResponseDto.of(lounge);
    }

    public List<LoungeInfoDto> findLoungeByUserId(Long userId) {
        return loungeRepository.findLoungeByUserId(userId).stream()
                .filter(lounge -> lounge.getStatus().isActive())
                .map(LoungeInfoDto::of)
                .toList();
    }

    public Lounge findLoungeById(Long loungeId) {
        return loungeRepository.findById(loungeId)
                .orElseThrow(() -> new LoungeException(LoungeExceptionType.INVALID_LOUNGE_ID_EXCEPTION));
    }

    public LoungeDetailInfoDto createLoungeDetailInfo(
            Long loungeId,
            List<LoungeDetailInfoDto.ObjetInfo> objetInfos
    ) {
        Lounge findLounge = loungeRepository.findById(loungeId)
                .orElseThrow(() -> new LoungeException(LoungeExceptionType.INVALID_LOUNGE_ID_EXCEPTION));
        return LoungeDetailInfoDto.builder()
                .loungeId(loungeId)
                .name(findLounge.getName())
                .type(findLounge.getType().getTypeName())
                .userId(findLounge.getUser().getId())
                .objets(objetInfos)
                .build();
    }
}
