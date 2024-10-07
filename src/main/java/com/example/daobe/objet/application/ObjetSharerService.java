package com.example.daobe.objet.application;

import com.example.daobe.objet.application.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.application.dto.ObjetSharerResponseDto;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharer;
import com.example.daobe.objet.domain.event.ObjetInviteEvent;
import com.example.daobe.objet.domain.repository.ObjetSharerRepository;
import com.example.daobe.user.application.UserService;
import com.example.daobe.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ObjetSharerService {

    private final ObjetSharerRepository objetSharerRepository;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    public List<ObjetSharer> getRecentObjetSharerList(Long userId) {
        return objetSharerRepository.findTop4ByUserIdAndActiveStatus(userId);
    }

    public void createAndSaveObjetSharer(ObjetCreateRequestDto request, Long userId, Objet objet) {
        List<Long> sharerIdList = request.sharers();
        sharerIdList.add(userId);
        List<Long> distinctSharerIdList = sharerIdList.stream().distinct().toList();

        List<User> userList = userService.getUserListByIdList(distinctSharerIdList);

        List<ObjetSharer> objetSharerList = userList.stream()
                .map(user -> ObjetSharer.builder()
                        .user(user)
                        .objet(objet)
                        .build())
                .toList();

        objetSharerRepository.saveAll(objetSharerList);
        objetSharerList.forEach(objetSharer ->
                eventPublisher.publishEvent(new ObjetInviteEvent(userId, objetSharer))
        );
    }

    public void updateObjetSharerList(Objet objet, List<Long> sharerIdList) {
        sharerIdList.add(objet.getUser().getId());
        List<Long> distinctSharerIdList = sharerIdList.stream().distinct().toList();
        List<Long> currentObjetSharerIdList = objetSharerRepository.findSharerIdListByObjet(objet);

        List<Long> deleteObjetSharerIdList = currentObjetSharerIdList.stream()
                .filter(sharerId -> !distinctSharerIdList.contains(sharerId))
                .toList();

        objetSharerRepository.deleteAllByObjetAndUserIdIn(objet, deleteObjetSharerIdList);

        List<ObjetSharer> newObjetSharerList = distinctSharerIdList.stream()
                .filter(sharerId -> !currentObjetSharerIdList.contains(sharerId))
                .map((sharerId) -> createSharer(sharerId, objet))
                .toList();

        objetSharerRepository.saveAll(newObjetSharerList);
        newObjetSharerList.forEach(
                objetSharer -> eventPublisher.publishEvent(
                        new ObjetInviteEvent(objet.getUser().getId(), objetSharer)
                )
        );
    }

    @Transactional
    public ObjetSharerResponseDto getObjetSharerList(Long objetId) {
        List<ObjetSharer> objetSharerList = objetSharerRepository.findAllByObjetIdAndActiveStatus(objetId);
        return ObjetSharerResponseDto.of(objetSharerList);
    }

    private ObjetSharer createSharer(Long sharerId, Objet objet) {
        return ObjetSharer.builder()
                .user(userService.getUserById(sharerId))
                .objet(objet)
                .build();
    }
}

