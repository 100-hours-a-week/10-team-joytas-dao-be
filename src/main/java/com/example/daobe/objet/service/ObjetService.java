package com.example.daobe.objet.service;

import com.example.daobe.lounge.entity.Lounge;
import com.example.daobe.lounge.repository.LoungeRepository;
import com.example.daobe.objet.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.dto.ObjetCreateResponseDto;
import com.example.daobe.objet.dto.ObjetDetailInfoDto;
import com.example.daobe.objet.dto.ObjetInfoDto;
import com.example.daobe.objet.dto.ObjetUpdateRequestDto;
import com.example.daobe.objet.entity.Objet;
import com.example.daobe.objet.entity.ObjetStatus;
import com.example.daobe.objet.entity.ObjetType;
import com.example.daobe.objet.repository.ObjetRepository;
import com.example.daobe.shared.entity.UserObjet;
import com.example.daobe.shared.repository.UserObjetRepository;
import com.example.daobe.user.entity.User;
import com.example.daobe.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObjetService {

    private static final String NOT_EXISTS_OBJET_EXCEPTION = "NOT_EXISTS_OBJET_EXCEPTION";
    private static final String INVALID_LOUNGE_ID_EXCEPTION = "INVALID_LOUNGE_ID_EXCEPTION";
    private static final String INVALID_USER_ID_EXCEPTION = "INVALID_USER_ID_EXCEPTION";
    private static final String INVALID_OBJET_ID_EXCEPTION = "INVALID_OBJET_ID_EXCEPTION";
    private static final String NO_PERMISSIONS_ON_OBJET = "NO_PERMISSIONS_ON_OBJET";

    private final ObjetRepository objetRepository;
    private final LoungeRepository loungeRepository;
    private final UserRepository userRepository;
    private final UserObjetRepository userObjetRepository;

    public ObjetCreateResponseDto create(Long userId, ObjetCreateRequestDto request, String imageUrl) {
        Lounge lounge = loungeRepository.findById(request.loungeId())
                // TODO : custom exception 만들어서 처리
                .orElseThrow(() -> new IllegalArgumentException(INVALID_LOUNGE_ID_EXCEPTION));

        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_USER_ID_EXCEPTION));

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
                            .orElseThrow(() -> new IllegalArgumentException(INVALID_USER_ID_EXCEPTION));
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

    @Transactional
    public ObjetCreateResponseDto update(Long userId, ObjetUpdateRequestDto request) {
        // Objet 찾기
        Objet findObjet = objetRepository.findById(request.objetId())
                .orElseThrow(() -> new IllegalArgumentException(INVALID_OBJET_ID_EXCEPTION));

        // NOTE : 우선 권한의 주체는 생성자로 하되, 도메인 별 권한의 주체를 고려해봐야 한다.
        // 해당 유저가 생성한 오브제가 아닌 경우
        if (!findObjet.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(NO_PERMISSIONS_ON_OBJET);
        }

        // Objet 업데이트
        findObjet.updateDetails(request.name(), request.description());

        // 기존 관계에서 ID만 추출하여 Set으로 관리
        Set<Long> currentOwnerIds = findObjet.getUserObjets().stream()
                .map(userObjet -> userObjet.getUser().getId())
                .collect(Collectors.toSet());

        // 새로운 관계에서 ID를 Set으로 관리
        Set<Long> newOwnerIds = new HashSet<>(request.owners());

        // 추가된 관계 삽입
        for (Long newOwnerId : newOwnerIds) {
            if (!currentOwnerIds.contains(newOwnerId)) {
                User user = userRepository.findById(newOwnerId)
                        .orElseThrow(() -> new IllegalArgumentException(INVALID_USER_ID_EXCEPTION));

                UserObjet newUserObjet = UserObjet.builder()
                        .user(user)
                        .objet(findObjet)
                        .build();
                userObjetRepository.save(newUserObjet);

                // 관계 설정
                findObjet.getUserObjets().add(newUserObjet);
            }
        }

        // 제거된 관계 삭제
        findObjet.getUserObjets().removeIf(userObjet -> {
            if (!newOwnerIds.contains(userObjet.getUser().getId())) {
                userObjetRepository.delete(userObjet);
                return true;
            }
            return false;
        });

        // Objet 엔티티 저장
        objetRepository.save(findObjet);

        return ObjetCreateResponseDto.of(findObjet);
    }


    public ObjetCreateResponseDto updateWithFile(Long userId, ObjetUpdateRequestDto request, String imageUrl) {
        // Objet 찾기
        Objet findObjet = objetRepository.findById(request.objetId())
                .orElseThrow(() -> new IllegalArgumentException(INVALID_OBJET_ID_EXCEPTION));

        // 해당 유저가 생성한 오브제가 아닌 경우
        if (!findObjet.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(NO_PERMISSIONS_ON_OBJET);
        }

        // Objet 업데이트
        findObjet.updateDetailsWithImage(request.name(), request.description(), imageUrl);

        // 기존 관계에서 ID만 추출하여 Set으로 관리
        Set<Long> currentOwnerIds = findObjet.getUserObjets().stream()
                .map(userObjet -> userObjet.getUser().getId())
                .collect(Collectors.toSet());

        // 새로운 관계에서 ID를 Set으로 관리
        Set<Long> newOwnerIds = new HashSet<>(request.owners());

        // 추가된 관계 삽입
        for (Long newOwnerId : newOwnerIds) {
            if (!currentOwnerIds.contains(newOwnerId)) {
                User user = userRepository.findById(newOwnerId)
                        .orElseThrow(() -> new IllegalArgumentException(INVALID_USER_ID_EXCEPTION));

                UserObjet newUserObjet = UserObjet.builder()
                        .user(user)
                        .objet(findObjet)
                        .build();
                userObjetRepository.save(newUserObjet);

                // 관계 설정
                findObjet.getUserObjets().add(newUserObjet);
            }
        }

        // 제거된 관계 삭제
        findObjet.getUserObjets().removeIf(userObjet -> {
            if (!newOwnerIds.contains(userObjet.getUser().getId())) {
                userObjetRepository.delete(userObjet);
                return true;
            }
            return false;
        });

        // Objet 엔티티 저장
        objetRepository.save(findObjet);

        return ObjetCreateResponseDto.of(findObjet);
    }

    public List<ObjetInfoDto> getObjetList(Long userId, Long loungeId, Boolean owner) {
        if (Boolean.TRUE.equals(owner)) {
            return objetRepository.findObjetListForOwner(userId, loungeId).stream()
                    .map(ObjetInfoDto::of)
                    .toList();
        } else {
            return objetRepository.findObjetList(loungeId).stream()
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
