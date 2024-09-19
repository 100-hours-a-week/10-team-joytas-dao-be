package com.example.daobe.objet.application;

import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_SHARER_EXCEPTION;

import com.example.daobe.lounge.domain.repository.LoungeSharerRepository;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.objet.application.dto.ObjetSignalRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ObjetSignalService {

    // FIXME: 추후에 Objet 도메인이 아니라 Lounge 도메인으로 옮겨야한다

    private final LoungeSharerRepository loungeSharerRepository;

    public void isObjetSharer(Long userId, ObjetSignalRequestDto request) {
        boolean isLoungeSharer = loungeSharerRepository.existsActiveLoungeSharerByUserIdAndLoungeId(userId,
                request.loungeId());
        if (!isLoungeSharer) {
            throw new LoungeException(INVALID_LOUNGE_SHARER_EXCEPTION);
        }
    }
}
