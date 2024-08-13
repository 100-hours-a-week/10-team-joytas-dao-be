package com.example.daobe.lounge.service;

import com.example.daobe.lounge.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.dto.LoungeDetailInfoDto;
import com.example.daobe.lounge.dto.LoungeInfoDto;
import com.example.daobe.lounge.entity.Lounge;
import com.example.daobe.objet.repository.ObjetRepository;
import com.example.daobe.user.entity.User;
import com.example.daobe.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoungeFacadeService {

    private final LoungeService loungeService;
    private final UserRepository userRepository;
    private final ObjetRepository objetRepository;

    public LoungeCreateResponseDto create(LoungeCreateRequestDto request) {
        User findUser = findUserById(request.userId());
        return loungeService.create(request, findUser);
    }

    public LoungeDetailInfoDto getLoungeDetail(Long loungeId) {
        List<LoungeDetailInfoDto.ObjetInfo> objetInfos = objetRepository.findAll().stream()
                .map(LoungeDetailInfoDto.ObjetInfo::of)
                .toList();
        return loungeService.createLoungeDetailInfo(loungeId, objetInfos);
    }

    // find
    public List<LoungeInfoDto> findLoungeByUserId(Long userId) {
        return loungeService.findLoungeByUserId(userId);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NOT_EXISTS_USER_EXCEPTION"));
    }

    private Lounge findLoungeById(Long loungeId) {
        return loungeService.findLoungeById(loungeId);
    }
}
