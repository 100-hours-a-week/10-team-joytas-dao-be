package com.example.daobe.lounge.application;

import static com.example.daobe.lounge.exception.LoungeExceptionType.ALREADY_INVITED_USER_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_SHARER_EXCEPTION;

import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.LoungeSharer;
import com.example.daobe.lounge.domain.repository.LoungeSharerRepository;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoungeSharerService {

    private final LoungeSharerRepository loungeSharerRepository;

    public void createAndSaveLoungeSharer(User user, Lounge lounge) {
        LoungeSharer loungeSharer = LoungeSharer.builder()
                .user(user)
                .lounge(lounge)
                .build();
        loungeSharerRepository.save(loungeSharer);
    }

    public LoungeSharer inviteUser(User user, Lounge lounge, Long inviterId) {
        // 활성 상태인 라운지인지 검증
        lounge.isActiveOrThrow();

        // 초대자가 라운지에 소속되어있는지 검증
        if (isNotExistUserInLounge(inviterId, lounge.getId())) {
            throw new LoungeException(INVALID_LOUNGE_SHARER_EXCEPTION);
        }

        // 초대 대상자 라운지 소속 중복 여부 검증
        if (isNotExistUserInLounge(user.getId(), lounge.getId())) {
            LoungeSharer loungeSharer = LoungeSharer.builder()
                    .user(user)
                    .lounge(lounge)
                    .build();
            loungeSharerRepository.save(loungeSharer);
            return loungeSharer;
        }
        throw new LoungeException(ALREADY_INVITED_USER_EXCEPTION);
    }

    private boolean isNotExistUserInLounge(Long userId, Long loungeId) {
        return !loungeSharerRepository.existsByUserIdAndLoungeId(userId, loungeId);
    }
}
