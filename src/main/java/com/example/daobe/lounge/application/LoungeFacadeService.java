package com.example.daobe.lounge.application;

import com.example.daobe.lounge.application.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.application.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.application.dto.LoungeDetailResponseDto;
import com.example.daobe.lounge.application.dto.LoungeInfoResponseDto;
import com.example.daobe.lounge.application.dto.LoungeInviteRequestDto;
import com.example.daobe.lounge.application.dto.LoungeSharerInfoResponseDto;
import com.example.daobe.lounge.application.dto.LoungeValidateRequestDto;
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

    @Transactional
    public LoungeCreateResponseDto createLounge(LoungeCreateRequestDto request, Long userId) {
        User findUser = userService.getUserById(userId);
        Lounge createdLounge = loungeService.createAndSaveLounge(request, findUser);
        loungeSharerService.createAndSaveLoungeSharer(findUser, createdLounge);
        return LoungeCreateResponseDto.of(createdLounge);
    }

    public List<LoungeInfoResponseDto> getAllLounges(Long userId) {
        return loungeService.getLoungeInfosByUserId(userId);
    }

    public LoungeDetailResponseDto getLoungeDetail(Long userId, Long loungeId) {
        Lounge findLounge = loungeService.getLoungeById(loungeId);
        loungeSharerService.validateLoungeSharer(userId, loungeId);
        return loungeService.getLoungeDetailInfo(findLounge);
    }

    @Transactional
    public void deleteLounge(Long userId, Long loungeId) {
        Lounge lounge = loungeService.getLoungeById(loungeId);
        loungeService.deleteLoungeByUserId(userId, lounge);
    }

    @Transactional
    public void inviteUser(LoungeInviteRequestDto request, Long inviterId) {
        User findUser = userService.getUserById(request.userId());
        Lounge findLounge = loungeService.getLoungeById(request.loungeId());
        loungeSharerService.inviteUser(findUser, findLounge, inviterId);
    }

    @Transactional
    public void updateInvitedUserStatus(Long userId, Long loungeId) {
        User invitedUser = userService.getUserById(userId);
        Lounge findLounge = loungeService.getLoungeById(loungeId);
        loungeSharerService.updateInvitedUserStatus(invitedUser.getId(), findLounge.getId());
    }

    public List<LoungeSharerInfoResponseDto> searchLoungeSharer(Long userId, String nickname, Long loungeId) {
        Lounge findLounge = loungeService.getLoungeById(loungeId);
        return loungeSharerService.searchLoungeSharer(userId, nickname, findLounge);
    }

    @Transactional
    public void withdraw(Long userId, Long loungeId) {
        User findUser = userService.getUserById(userId);
        Lounge findLounge = loungeService.getLoungeById(loungeId);
        loungeSharerService.withdraw(findUser, findLounge);
    }

    public void isLoungeSharer(Long userId, LoungeValidateRequestDto request) {
        loungeSharerService.validateLoungeSharer(userId, request.loungeId());
    }
}
