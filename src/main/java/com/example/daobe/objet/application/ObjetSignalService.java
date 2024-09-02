package com.example.daobe.objet.application;

import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_ID_EXCEPTION;

import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.repository.LoungeRepository;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.objet.application.dto.ObjetSignalRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ObjetSignalService {

    private final LoungeRepository loungeRepository;

    public boolean isObjetSharer(Long userId, ObjetSignalRequestDto request) {
        Lounge findLounge = loungeRepository.findById(request.loungeId())
                .orElseThrow(() -> new LoungeException(INVALID_LOUNGE_ID_EXCEPTION));
        return findLounge.getLoungeSharers().stream()
                .anyMatch(loungeSharer -> loungeSharer.getUser().getId().equals(userId));
    }
}
