package com.example.daobe.lounge.service;

import com.example.daobe.lounge.dto.LoungeCreateRequestDto;
import com.example.daobe.lounge.dto.LoungeCreateResponseDto;
import com.example.daobe.lounge.dto.LoungeDetailInfoDto;
import com.example.daobe.lounge.dto.LoungeInfoDto;
import com.example.daobe.lounge.dto.LoungeInviteDto;
import com.example.daobe.lounge.entity.InviteStatus;
import com.example.daobe.lounge.entity.Lounge;
import com.example.daobe.objet.repository.ObjetRepository;
import com.example.daobe.shared.entity.UserLounge;
import com.example.daobe.shared.repository.UserLoungeRepository;
import com.example.daobe.user.entity.User;
import com.example.daobe.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoungeFacadeService {

    private final LoungeService loungeService;
    private final UserRepository userRepository;
    private final ObjetRepository objetRepository;
    private final UserLoungeRepository userLoungeRepository;

    public LoungeCreateResponseDto create(LoungeCreateRequestDto request) {
        User findUser = findUserById(request.userId());
        return loungeService.createLounge(request, findUser);
    }

    public LoungeDetailInfoDto getLoungeDetail(Long loungeId) {
        List<LoungeDetailInfoDto.ObjetInfo> objetInfos = objetRepository.findAll().stream()
                .map(LoungeDetailInfoDto.ObjetInfo::of)
                .toList();
        return loungeService.createLoungeDetailInfo(loungeId, objetInfos);
    }

    @Transactional
    public InviteStatus inviteUser(LoungeInviteDto request) {
        User findUser = findUserById(request.userId());
        Lounge findLounge = findLoungeById(request.loungeId());

        // 해당 라운지에 이미 소속되어 있는지 확인
        if (isNotExistUserInLounge(findUser, findLounge)) {
            UserLounge userLounge = UserLounge.builder()
                    .user(findUser)
                    .lounge(findLounge)
                    .build();
            userLoungeRepository.save(userLounge);
            return InviteStatus.SUCCESS;
        }
        return InviteStatus.ALREADY_EXISTS;
    }

    // find
    public List<LoungeInfoDto> findLoungeByUserId(Long userId) {
        return loungeService.findLoungeByUserId(userId);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NOT_EXISTS_USER_EXCEPTION"));
    }

    private Lounge findLoungeById(Long id) {
        return loungeService.findLoungeById(id);
    }

    private boolean isNotExistUserInLounge(User user, Lounge lounge) {
        return !userLoungeRepository.existsByUserIdAndLoungeId(user.getId(), lounge.getId());
    }
}
