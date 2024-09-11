package com.example.daobe.lounge.application;

import static com.example.daobe.lounge.exception.LoungeExceptionType.ALREADY_INVITED_USER_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_SHARER_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.MAXIMUM_LOUNGE_LIMIT_EXCEEDED_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.NOT_ALLOW_LOUNGE_WITHDRAW_EXCEPTION;

import com.example.daobe.lounge.application.dto.LoungeSharerInfoResponseDto;
import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.LoungeSharer;
import com.example.daobe.lounge.domain.event.LoungeInviteEvent;
import com.example.daobe.lounge.domain.repository.LoungeSharerRepository;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.user.domain.User;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

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

    public List<LoungeSharerInfoResponseDto> searchLoungeSharer(Long userId, String nickname, Long loungeId) {
        // 초대자가 라운지에 소속되어있는지 검증
        if (!isExistUserInLounge(userId, loungeId)) {
            throw new LoungeException(INVALID_LOUNGE_SHARER_EXCEPTION);
        }

        List<LoungeSharer> byUserId = loungeSharerRepository
                .findByLounge_IdAndUser_NicknameContaining(loungeId, nickname);
        return LoungeSharerInfoResponseDto.of(byUserId);
    }

    public void withdraw(User user, Lounge lounge) {
        lounge.isActiveOrThrow();
        if (!isExistUserInLounge(user.getId(), lounge.getId())) {
            throw new LoungeException(INVALID_LOUNGE_SHARER_EXCEPTION);
        }
        if (isLoungeCreator(user, lounge.getUser())) {
            throw new LoungeException(NOT_ALLOW_LOUNGE_WITHDRAW_EXCEPTION);
        }
        loungeSharerRepository.deleteByUserIdAndLoungeId(user.getId(), lounge.getId());
    }

    // TODO: 추후 도메인 로직으로 분리
    private void validateInvite(User user, Lounge lounge, Long inviterId) {
        lounge.isActiveOrThrow();

        if (!isExistUserInLounge(inviterId, lounge.getId())) {
            throw new LoungeException(INVALID_LOUNGE_SHARER_EXCEPTION);
        }

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

    private boolean isLoungeCreator(User user, User loungeUser) {
        return Objects.equals(user, loungeUser);
    }
}
