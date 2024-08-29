package com.example.daobe.lounge.application;

import com.example.daobe.lounge.application.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.application.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.application.dto.LoungeDetailInfoDto;
import com.example.daobe.lounge.application.dto.LoungeInfoDto;
import com.example.daobe.lounge.application.dto.LoungeInviteDto;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.LoungeSharer;
import com.example.daobe.lounge.domain.event.LoungeInviteEvent;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoungeFacadeService {

    private final UserService userService;
    private final LoungeService loungeService;
    private final LoungeSharerService loungeSharerService;
    private final ApplicationEventPublisher eventPublisher;

    // 라운지 생성
    @Transactional
    public LoungeCreateResponseDto createLounge(LoungeCreateRequestDto request, Long userId) {
        User findUser = userService.getUserById(userId);
        Lounge createdLounge = loungeService.createAndSaveLounge(request, findUser);
        loungeSharerService.createAndSaveLoungeSharer(findUser, createdLounge);
        return LoungeCreateResponseDto.of(createdLounge);
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
        LoungeSharer loungeSharer = loungeSharerService.inviteUser(findUser, findLounge, inviterId);

        // 초대 성공시 이벤트 발행
        if (loungeSharer != null) {
            eventPublisher.publishEvent(new LoungeInviteEvent(inviterId, loungeSharer));
        }
    }
}
