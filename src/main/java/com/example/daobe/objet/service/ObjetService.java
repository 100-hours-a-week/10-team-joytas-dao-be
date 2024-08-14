package com.example.daobe.objet.service;

import com.example.daobe.lounge.entity.Lounge;
import com.example.daobe.lounge.repository.LoungeRepository;
import com.example.daobe.objet.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.dto.ObjetCreateResponseDto;
import com.example.daobe.objet.dto.ObjetDetailInfoDto;
import com.example.daobe.objet.dto.ObjetInfoDto;
import com.example.daobe.objet.entity.Objet;
import com.example.daobe.objet.entity.ObjetStatus;
import com.example.daobe.objet.entity.ObjetType;
import com.example.daobe.objet.repository.ObjetRepository;
import com.example.daobe.shared.entity.UserObjet;
import com.example.daobe.shared.repository.UserObjetRepository;
import com.example.daobe.user.entity.User;
import com.example.daobe.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObjetService {

    private static final String NOT_EXISTS_OBJET_EXCEPTION = "NOT_EXISTS_OBJET_EXCEPTION";

    private final ObjetRepository objetRepository;
    private final LoungeRepository loungeRepository;
    private final UserRepository userRepository;
    private final UserObjetRepository userObjetRepository;

    public ObjetCreateResponseDto create(ObjetCreateRequestDto request, String imageUrl) {
        Lounge lounge = loungeRepository.findById(request.loungeId())
                // TODO : custom exception 만들어서 처리
                .orElseThrow(() -> new IllegalArgumentException("Invalid Lounge ID"));

        // TODO : 요청 보낸 유저의 ID로 변경
        User creator = userRepository.findById(1001L)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));

        Objet objet = Objet.builder()
                .name(request.name())
                .explanation(request.description())
                .type(ObjetType.from(request.type()))
                .status(ObjetStatus.ACTIVE)
                .user(creator)
                .lounge(lounge)
                .imageUrl((imageUrl))
                .build();

        objetRepository.save(objet);

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

        userObjetRepository.saveAll(userObjets);

        objet.updateUserObjets(userObjets);

        return ObjetCreateResponseDto.of(objet);
    }

    public List<ObjetInfoDto> getObjetList(Long lounge_id, Boolean owner) {
        // NOTE : owner == true 이면, 본인을 대상으로 한 오브제 목록 조회
        // TODO : 인증 추가 후, 본인을 대상으로 한 오브제 목록 조회 기능 추가
        if (Boolean.TRUE.equals(owner)) {
            return objetRepository.findObjetListForOwner(lounge_id).stream()
                    .map(ObjetInfoDto::of)
                    .toList();
        } else {
            return objetRepository.findObjetList(lounge_id).stream()
                    .map(ObjetInfoDto::of)
                    .toList();
        }
    }

    public ObjetDetailInfoDto getObjetDetail(Long objetId) {
        Objet findObjet = objetRepository.findById(objetId)
                .orElseThrow(() -> new RuntimeException(NOT_EXISTS_OBJET_EXCEPTION));
        return ObjetDetailInfoDto.builder()
                .objetId(objetId)
                .name(findObjet.getName())
                .userId(findObjet.getUser().getId())
                .nickname(findObjet.getUser().getNickname())
                .objetImage(findObjet.getImageUrl())
                .description(findObjet.getExplanation())
                // TODO : 실시간 오브제 상태 확인 로직 구현 후 변경
                .isActive(true)
                // TODO : 실시간 오브제 접속 유저 목록 로직 구현 후 변경
                .viewers(null)
                // TODO : 오브제 최근 채팅 목록 로직 구현 후 변경
                .chattings(null)
                // TODO : 오브제 음성 채팅 참가자 수 로직 구현 후 변경
                .CallingUserNum(3L)
                .build();
    }

}
