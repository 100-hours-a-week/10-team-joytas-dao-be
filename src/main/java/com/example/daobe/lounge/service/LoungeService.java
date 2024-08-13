package com.example.daobe.lounge.service;

import com.example.daobe.lounge.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.entity.Lounge;
import com.example.daobe.lounge.entity.LoungeStatus;
import com.example.daobe.lounge.repository.LoungeRepository;
import com.example.daobe.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoungeService {

    private final LoungeRepository loungeRepository;

    public LoungeCreateResponseDto create(LoungeCreateRequestDto request, User user) {
        Lounge lounge = Lounge.builder()
                .user(user)
                .name(request.name())
                .type(request.type())
                .status(LoungeStatus.ACTIVE)    // 라운지 활성화
                .reason(null)
                .reasonDetail(null)
                .build();

        Lounge savedLounge = loungeRepository.save(lounge);
        return savedLounge.toLoungeCreateResponseDto();
    }
}
