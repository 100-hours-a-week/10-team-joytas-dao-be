package com.example.daobe.objet.service;

import com.example.daobe.lounge.entity.Lounge;
import com.example.daobe.lounge.repository.LoungeRepository;
import com.example.daobe.objet.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.dto.ObjetCreateResponseDto;
import com.example.daobe.objet.entity.ObjectStatus;
import com.example.daobe.objet.entity.Objet;
import com.example.daobe.objet.entity.ObjetType;
import com.example.daobe.objet.repository.ObjetRepository;
import com.example.daobe.shared.entity.UserObjet;
import com.example.daobe.user.entity.User;
import com.example.daobe.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObjetService {

    private final ObjetRepository objetRepository;
    private final LoungeRepository loungeRepository;
    private final UserRepository userRepository;

    public ObjetCreateResponseDto create(ObjetCreateRequestDto request) {
        Lounge lounge = loungeRepository.findById(request.loungeId())
                // TODO : custom exception 만들어서 처리
                .orElseThrow(() -> new IllegalArgumentException("Invalid Lounge ID"));

        Objet objet = Objet.builder()
                .name(request.name())
                .explanation(request.description())
                .type(ObjetType.from(request.type()))
                .status(ObjectStatus.ACTIVE)
                .lounge(lounge)
                .build();

        List<UserObjet> userObjets = request.owners().stream()
                .map(ownerId -> {
                    User user = userRepository.findById(ownerId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));
                    return UserObjet.builder()
                            .user(user)
                            .objet(objet)
                            .build();
                })
                .toList();

        objet.updateUserObjets(userObjets);
        objetRepository.save(objet);

        return ObjetCreateResponseDto.of(objet);
    }
}
