package com.example.daobe.lounge.service;

import com.example.daobe.lounge.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.dto.LoungeCreateResponseDto;
import com.example.daobe.user.entity.User;
import com.example.daobe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoungeFacadeService {

    private final UserRepository userRepository;
    private final LoungeService loungeService;

    public LoungeCreateResponseDto create(LoungeCreateRequestDto request) {
        User findUser = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("NOT_EXISTS_USER_EXCEPTION"));
        return loungeService.create(request, findUser);
    }
}
