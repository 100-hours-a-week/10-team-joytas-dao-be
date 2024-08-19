package com.example.daobe.objet.application;

import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_ID_EXCEPTION;
import static com.example.daobe.objet.exception.ObjetExceptionType.INVALID_OBJET_ID_EXCEPTION;
import static com.example.daobe.objet.exception.ObjetExceptionType.NO_PERMISSIONS_ON_OBJET;
import static com.example.daobe.user.exception.UserExceptionType.NOT_EXIST_USER;

import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.repository.LoungeRepository;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.objet.application.dto.ObjetCreateRequestDto;
import com.example.daobe.objet.application.dto.ObjetCreateResponseDto;
import com.example.daobe.objet.application.dto.ObjetDetailInfoDto;
import com.example.daobe.objet.application.dto.ObjetInfoDto;
import com.example.daobe.objet.application.dto.ObjetMeInfoDto;
import com.example.daobe.objet.application.dto.ObjetUpdateRequestDto;
import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.ObjetSharer;
import com.example.daobe.objet.domain.ObjetStatus;
import com.example.daobe.objet.domain.ObjetType;
import com.example.daobe.objet.domain.repository.ObjetRepository;
import com.example.daobe.objet.domain.repository.ObjetSharerRepository;
import com.example.daobe.objet.exception.ObjetException;
import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.repository.UserRepository;
import com.example.daobe.user.exception.UserException;
import jakarta.transaction.Transactional;
import java.util.Comparator;
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

    private final ObjetRepository objetRepository;
    private final LoungeRepository loungeRepository;
    private final UserRepository userRepository;
    private final ObjetSharerRepository objetSharerRepository;

    public ObjetCreateResponseDto create(Long userId, ObjetCreateRequestDto request, String imageUrl) {
        Lounge lounge = loungeRepository.findById(request.loungeId())
                .orElseThrow(() -> new LoungeException(INVALID_LOUNGE_ID_EXCEPTION));
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));

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

        // 생성자를 sharer에 추가
        request.sharers().add(userId);

        List<ObjetSharer> objetSharers = request.sharers().stream()
                .map(sharerId -> {
                    User user = userRepository.findById(sharerId)
                            .orElseThrow(() -> new UserException(NOT_EXIST_USER));
                    return ObjetSharer.builder()
                            .user(user)
                            .objet(objet)
                            .build();
                })
                .toList();

        objetSharerRepository.saveAll(objetSharers);

        objet.updateUserObjets(objetSharers);

        return ObjetCreateResponseDto.of(objet);
    }

    @Transactional
    public ObjetCreateResponseDto update(Long userId, ObjetUpdateRequestDto request) {
        // Objet 찾기
        Objet findObjet = objetRepository.findById(request.objetId())
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));

        // NOTE : 우선 권한의 주체는 생성자로 하되, 도메인 별 권한의 주체를 고려해봐야 한다.
        // 해당 유저가 생성한 오브제가 아닌 경우
        validateObjetOwner(findObjet, userId);

        // Objet 업데이트
        findObjet.updateDetails(request.name(), request.description());

        // 기존 관계에서 ID만 추출하여 Set으로 관리
        Set<Long> currentSharerIds = findObjet.getObjetSharers().stream()
                .map(objetSharer -> objetSharer.getUser().getId())
                .collect(Collectors.toSet());

        // 생성자를 sharer에 추가
        request.sharers().add(userId);

        // 새로운 관계에서 ID를 Set으로 관리
        Set<Long> newSharerIds = new HashSet<>(request.sharers());

        // 추가된 관계 삽입
        for (Long newSharerId : newSharerIds) {
            if (!currentSharerIds.contains(newSharerId)) {
                User user = userRepository.findById(newSharerId)
                        .orElseThrow(() -> new UserException(NOT_EXIST_USER));

                ObjetSharer newObjetSharer = ObjetSharer.builder()
                        .user(user)
                        .objet(findObjet)
                        .build();
                objetSharerRepository.save(newObjetSharer);

                // 관계 설정
                findObjet.getObjetSharers().add(newObjetSharer);
            }
        }

        // 제거된 관계 삭제
        findObjet.getObjetSharers().removeIf(objetSharer -> {
            if (!newSharerIds.contains(objetSharer.getUser().getId())) {
                objetSharerRepository.delete(objetSharer);
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
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));

        // 해당 유저가 생성한 오브제가 아닌 경우
        validateObjetOwner(findObjet, userId);

        // Objet 업데이트
        findObjet.updateDetailsWithImage(request.name(), request.description(), imageUrl);

        // 기존 관계에서 ID만 추출하여 Set으로 관리
        Set<Long> currentSharerId = findObjet.getObjetSharers().stream()
                .map(objetSharer -> objetSharer.getUser().getId())
                .collect(Collectors.toSet());

        // 생성자를 sharer에 추가
        request.sharers().add(userId);

        // 새로운 관계에서 ID를 Set으로 관리
        Set<Long> newSharerIds = new HashSet<>(request.sharers());

        // 추가된 관계 삽입
        for (Long newSharerId : newSharerIds) {
            if (!currentSharerId.contains(newSharerId)) {
                User user = userRepository.findById(newSharerId)
                        .orElseThrow(() -> new UserException(NOT_EXIST_USER));

                ObjetSharer newObjetSharer = ObjetSharer.builder()
                        .user(user)
                        .objet(findObjet)
                        .build();
                objetSharerRepository.save(newObjetSharer);

                // 관계 설정
                findObjet.getObjetSharers().add(newObjetSharer);
            }
        }

        // 제거된 관계 삭제
        findObjet.getObjetSharers().removeIf(objetSharer -> {
            if (!newSharerIds.contains(objetSharer.getUser().getId())) {
                objetSharerRepository.delete(objetSharer);
                return true;
            }
            return false;
        });

        // Objet 엔티티 저장
        objetRepository.save(findObjet);

        return ObjetCreateResponseDto.of(findObjet);
    }

    public List<ObjetInfoDto> getObjetList(Long userId, Long loungeId, Boolean sharer) {
        if (Boolean.TRUE.equals(sharer)) {
            List<ObjetSharer> ObjetSharerList = objetSharerRepository.findByUserId(userId);

            return objetRepository.findByLoungeIdAndDeletedAtIsNullAndStatusAndObjetSharersUserId(
                            loungeId,
                            ObjetStatus.ACTIVE,
                            userId
                    )
                    .stream()
                    .map(ObjetInfoDto::of)
                    .toList();
        } else {
            return objetRepository.findByLoungeIdAndDeletedAtIsNullAndStatus(loungeId, ObjetStatus.ACTIVE)
                    .stream()
                    .map(ObjetInfoDto::of)
                    .toList();
        }
    }

    public ObjetDetailInfoDto getObjetDetail(Long objetId) {
        Objet findObjet = objetRepository.findById(objetId)
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));
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

    public List<ObjetMeInfoDto> getMyRecentObjets(Long userId) {
        List<ObjetSharer> objetSharerList = objetSharerRepository.findByUserId(userId);

        // ObjetSharer 목록에서 Objet 엔티티만 추출합니다.
        List<Objet> objets = objetSharerList.stream()
                .map(ObjetSharer::getObjet)
                .filter(objet -> objet.getStatus() == ObjetStatus.ACTIVE
                        && objet.getDeletedAt() == null)
                .sorted(Comparator.comparing(Objet::getCreatedAt).reversed())
                .limit(4)
                .toList();

        return objets.stream()
                .map(ObjetMeInfoDto::of)
                .toList();
    }


    public ObjetCreateResponseDto delete(Long objetId, Long userId) {
        // 오브제 조회
        Objet findObjet = objetRepository.findById(objetId)
                .orElseThrow(() -> new ObjetException(INVALID_OBJET_ID_EXCEPTION));

        // 해당 유저가 생성한 오브제가 아닌 경우
        validateObjetOwner(findObjet, userId);

        // 오브제 삭제 - status 변경
        findObjet.updateStatus(ObjetStatus.DELETED);

        objetRepository.save(findObjet);

        return ObjetCreateResponseDto.of(findObjet);
    }

    private void validateObjetOwner(Objet findObjet, Long userId) {
        if (!findObjet.getUser().getId().equals(userId)) {
            throw new ObjetException(NO_PERMISSIONS_ON_OBJET);
        }
    }
}

