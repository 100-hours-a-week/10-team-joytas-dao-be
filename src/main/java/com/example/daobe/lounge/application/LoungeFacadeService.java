package com.example.daobe.lounge.application;

import com.example.daobe.lounge.application.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.application.dto.LoungeDetailInfoDto;
import com.example.daobe.lounge.application.dto.LoungeDto;
import com.example.daobe.lounge.application.dto.LoungeInfoDto;
import com.example.daobe.lounge.application.dto.LoungeInviteDto;
import com.example.daobe.lounge.application.dto.LoungeSharerInfoResponseDto;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoungeFacadeService {

    private final UserService userService;
    private final LoungeService loungeService;
    private final LoungeSharerService loungeSharerService;

    // 라운지 생성
    @Transactional
    public LoungeDto createLounge(LoungeCreateRequestDto request, Long userId) {
        User findUser = userService.getUserById(userId);
        Lounge createdLounge = loungeService.createAndSaveLounge(request, findUser);
        loungeSharerService.createAndSaveLoungeSharer(findUser, createdLounge);
        return LoungeDto.of(createdLounge);
    }

    // 라운지 목록 조회
    public List<LoungeInfoDto> getAllLounges(Long userId) {
        return loungeService.getLoungeInfosByUserId(userId);
    }

    // 라운지 상세 조회
    public LoungeDetailInfoDto getLoungeDetail(Long userId, Long loungeId) {
        Lounge findLounge = loungeService.getLoungeById(loungeId);
        return loungeService.getLoungeDetailInfo(userId, findLounge);
    }

    // 라운지 삭제
    @Transactional
    public void deleteLounge(Long userId, Long loungeId) {
        User findUser = userService.getUserById(userId);
        Lounge findLounge = loungeService.getLoungeById(loungeId);
        loungeService.deleteLoungeByUserId(findUser, findLounge);
    }

    // 라운지 초대
    @Transactional
    public void inviteUser(LoungeInviteDto request, Long inviterId) {
        User findUser = userService.getUserById(request.userId());
        Lounge findLounge = loungeService.getLoungeById(request.loungeId());
        loungeSharerService.inviteUser(findUser, findLounge, inviterId);
    }

    // 라운치 초대 수락/거절
    @Transactional
    public void updateInvitedUserStatus(Long userId, Long loungeId) {
        User invitedUser = userService.getUserById(userId);
        Lounge findLounge = loungeService.getLoungeById(loungeId);
        loungeSharerService.updateInvitedUserStatus(invitedUser, findLounge);
    }

    // 라운지 내 유저 검색
    public List<LoungeSharerInfoResponseDto> searchLoungeSharer(Long userId, String nickname, Long loungeId) {
        Lounge findLounge = loungeService.getLoungeById(loungeId);
        return loungeSharerService.searchLoungeSharer(userId, nickname, findLounge);
    }

    // 라운지 탈퇴
    @Transactional
    public void withdraw(Long userId, Long loungeId) {
        User findUser = userService.getUserById(userId);
        Lounge findLounge = loungeService.getLoungeById(loungeId);
        loungeSharerService.withdraw(findUser, findLounge);
    }

    // 라운지 공유자 검증
    public void isLoungeSharer(Long userId, LoungeDto request) {
        loungeSharerService.validateLoungeSharer(userId, request.loungeId());
    }
}
