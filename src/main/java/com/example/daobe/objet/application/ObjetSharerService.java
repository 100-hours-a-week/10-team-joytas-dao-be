package com.example.daobe.objet.application;

import static com.example.daobe.user.exception.UserExceptionType.NOT_EXIST_USER;

import com.example.daobe.objet.application.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.application.dto.ObjetUpdateRequestDto;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharer;
import com.example.daobe.objet.domain.event.ObjetInviteEvent;
import com.example.daobe.objet.domain.repository.ObjetSharerRepository;
import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.repository.UserRepository;
import com.example.daobe.user.exception.UserException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObjetSharerService {

    private final ObjetSharerRepository objetSharerRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void createAndSaveObjetSharer(ObjetCreateRequestDto request, User user, Objet objet) {
        List<Long> sharerIds = request.sharers();
        sharerIds.add(user.getId());

        List<ObjetSharer> objetSharers = sharerIds.stream()
                .map(sharerId -> {
                    User sharer = getUserById(sharerId);
                    ObjetSharer newObjetSharer = ObjetSharer.builder()
                            .user(sharer)
                            .objet(objet)
                            .build();
                    objetSharerRepository.save(newObjetSharer);
                    eventPublisher.publishEvent(new ObjetInviteEvent(sharer.getId(), newObjetSharer));
                    return newObjetSharer;
                })
                .toList();

        objet.updateUserObjets(objetSharers);
    }

    public void updateAndSaveObjetSharer(ObjetUpdateRequestDto request, User findUser, Objet findObjet) {
        Set<Long> currentSharerIds = getCurrentSharerIds(findObjet);
        List<Long> newSharerIds = request.sharers();
        newSharerIds.add(findUser.getId());

        newSharerIds.stream()
                .filter(newSharerId -> !currentSharerIds.contains(newSharerId))
                .forEach(newSharerId -> {
                    User user = getUserById(newSharerId);
                    ObjetSharer newObjetSharer = ObjetSharer.builder()
                            .user(user)
                            .objet(findObjet)
                            .build();
                    objetSharerRepository.save(newObjetSharer);
                    eventPublisher.publishEvent(new ObjetInviteEvent(findUser.getId(), newObjetSharer));
                    findObjet.getObjetSharers().add(newObjetSharer);
                });

        findObjet.getObjetSharers().removeIf(objetSharer -> {
            if (!newSharerIds.contains(objetSharer.getUser().getId())) {
                objetSharerRepository.delete(objetSharer);
                return true;
            }
            return false;
        });
    }

    private Set<Long> getCurrentSharerIds(Objet findObjet) {
        return findObjet.getObjetSharers().stream()
                .map(objetSharer -> objetSharer.getUser().getId())
                .collect(Collectors.toSet());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
    }
}
