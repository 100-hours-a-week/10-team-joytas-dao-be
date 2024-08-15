package com.example.daobe.lounge.service;

import com.example.daobe.lounge.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.dto.LoungeDetailInfoDto;
import com.example.daobe.lounge.dto.LoungeInfoDto;
import com.example.daobe.lounge.entity.Lounge;
import com.example.daobe.lounge.entity.LoungeStatus;
import com.example.daobe.lounge.entity.LoungeType;
import com.example.daobe.lounge.repository.LoungeRepository;
import com.example.daobe.user.entity.User;
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

    private static final String NOT_EXISTS_LOUNGE_EXCEPTION = "NOT_EXISTS_LOUNGE_EXCEPTION";

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
                .orElseThrow(() -> new RuntimeException(NOT_EXISTS_LOUNGE_EXCEPTION));
    }

    public LoungeDetailInfoDto createLoungeDetailInfo(
            Long loungeId,
            List<LoungeDetailInfoDto.ObjetInfo> objetInfos
    ) {
        Lounge findLounge = loungeRepository.findById(loungeId)
                .orElseThrow(() -> new RuntimeException(NOT_EXISTS_LOUNGE_EXCEPTION));
        return LoungeDetailInfoDto.builder()
                .loungeId(loungeId)
                .name(findLounge.getName())
                .type(findLounge.getType().getTypeName())
                .userId(findLounge.getUser().getId())
                .objets(objetInfos)
                .build();
    }
}
