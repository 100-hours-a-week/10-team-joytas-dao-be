package com.example.daobe.lounge.application;

import static com.example.daobe.lounge.exception.LoungeExceptionType.ALREADY_INVITED_USER_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.MAXIMUM_LOUNGE_LIMIT_EXCEEDED_EXCEPTION;

import com.example.daobe.lounge.application.dto.LoungeSharerInfoResponseDto;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.LoungeSharer;
import com.example.daobe.lounge.domain.LoungeSharerStatus;
import com.example.daobe.lounge.domain.event.LoungeInviteEvent;
import com.example.daobe.lounge.domain.repository.LoungeSharerRepository;
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
public class LoungeSharerService {

    private static final int MAX_LOUNGE_COUNT = 4;

    private final ApplicationEventPublisher eventPublisher;
    private final LoungeSharerRepository loungeSharerRepository;

    public void createAndSaveLoungeSharer(User user, Lounge lounge) {
        // 라운지 최대 개수를 초과하는지 검증
        if (isMaximumCountLounge(user.getId())) {
            throw new LoungeException(MAXIMUM_LOUNGE_LIMIT_EXCEEDED_EXCEPTION);
        }

        LoungeSharer loungeSharer = LoungeSharer.builder()
                .user(user)
                .lounge(lounge)
                .status(LoungeSharerStatus.ACTIVE)
                .build();
        loungeSharerRepository.save(loungeSharer);
    }

    public void inviteUser(User user, Lounge lounge, Long inviterId) {
        validateInvite(user, lounge, inviterId);
        LoungeSharer loungeSharer = LoungeSharer.builder()
                .user(user)
                .lounge(lounge)
                .build();
        loungeSharerRepository.save(loungeSharer);
        eventPublisher.publishEvent(new LoungeInviteEvent(inviterId, loungeSharer));
    }

    public void updateInvitedUserStatus(User user, Lounge lounge, boolean accept) {
        LoungeSharer findSharer = loungeSharerRepository.findByUserIdAndLoungeId(user.getId(), lounge.getId());
        if (accept && !findSharer.isActive()) {
            findSharer.update();
            loungeSharerRepository.save(findSharer);
        }
    }

    public List<LoungeSharerInfoResponseDto> searchLoungeSharer(Long userId, String nickname, Lounge lounge) {
        lounge.isSharerOrThrow(userId);
        List<LoungeSharer> byUserId = loungeSharerRepository
                .findByLounge_IdAndUser_NicknameContaining(lounge.getId(), nickname);
        return LoungeSharerInfoResponseDto.of(byUserId);
    }

    public void withdraw(User user, Lounge lounge) {
        lounge.isActiveOrThrow();
        lounge.isPossibleToWithdrawOrThrow(user.getId());
        loungeSharerRepository.deleteByUserIdAndLoungeId(user.getId(), lounge.getId());
    }

    // TODO: 추후 도메인 로직으로 분리
    private void validateInvite(User user, Lounge lounge, Long inviterId) {
        lounge.isActiveOrThrow();
        lounge.isSharerOrThrow(inviterId);

        if (isExistUserInLounge(user.getId(), lounge.getId())) {
            throw new LoungeException(ALREADY_INVITED_USER_EXCEPTION);
        }

        if (isMaximumCountLounge(user.getId())) {
            throw new LoungeException(MAXIMUM_LOUNGE_LIMIT_EXCEEDED_EXCEPTION);
        }
    }

    private boolean isExistUserInLounge(Long userId, Long loungeId) {
        return loungeSharerRepository.existsByUserIdAndLoungeId(userId, loungeId);
    }

    private boolean isMaximumCountLounge(Long userId) {
        return loungeSharerRepository.countActiveLoungeSharerByUserId(userId) == MAX_LOUNGE_COUNT;
    }
}
