package com.example.daobe.objet.application;

import static com.example.daobe.objet.exception.ObjetExceptionType.INVALID_OBJET_ID_EXCEPTION;

import com.example.daobe.objet.application.dto.ObjetSignalRequestDto;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.repository.ObjetRepository;
import com.example.daobe.objet.exception.ObjetException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ObjetSignalService {

    private final ObjetRepository objetRepository;

    public boolean isObjetSharer(Long userId, ObjetSignalRequestDto objetId) {
        Objet findObjet = objetRepository.findById(objetId.objetId())
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));

        return findObjet.getObjetSharers().stream()
                .anyMatch(sharer -> sharer.getUser().getId().equals(userId));
    }
}
