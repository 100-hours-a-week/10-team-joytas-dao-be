package com.example.daobe.objet.application;

import com.example.daobe.objet.application.dto.ObjetCallParticipantsResponseDto;
import com.example.daobe.objet.domain.repository.ObjetCallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ObjetSignalService {

    private final ObjetCallRepository objetCallRepository;

    public ObjetCallParticipantsResponseDto getCallParticipantsByObjetId(Long objetId) {
        return ObjetCallParticipantsResponseDto.of(objetCallRepository.getObjetCallCount(objetId));
    }
}
