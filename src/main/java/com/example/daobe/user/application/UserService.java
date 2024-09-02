package com.example.daobe.user.application;

import static com.example.daobe.user.exception.UserExceptionType.DUPLICATE_NICKNAME;
import static com.example.daobe.user.exception.UserExceptionType.NOT_EXIST_USER;

import com.example.daobe.user.application.dto.UpdateProfileRequestDto;
import com.example.daobe.user.application.dto.UpdateProfileResponseDto;
import com.example.daobe.user.application.dto.UserInfoResponseDto;
import com.example.daobe.user.application.dto.UserPokeRequestDto;
import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.UserStatus;
import com.example.daobe.user.domain.event.UserPokeEvent;
import com.example.daobe.user.domain.repository.UserPokeRepository;
import com.example.daobe.user.domain.repository.UserRepository;
import com.example.daobe.user.exception.UserException;
import com.example.daobe.user.exception.UserExceptionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPokeRepository userPokeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UserInfoResponseDto getUserInfoWithId(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
        return UserInfoResponseDto.of(findUser);
    }

    public List<UserInfoResponseDto> searchUserByNickname(String nickname) {
        List<User> findUserList = userRepository.findByNicknameContainingAndStatus(nickname, UserStatus.ACTIVE);
        return findUserList.stream()
                .map(UserInfoResponseDto::of)
                .toList();
    }

    public void checkValidateByNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new UserException(DUPLICATE_NICKNAME);
        }
    }

    @Transactional
    public UpdateProfileResponseDto updateNickname(Long userId, UpdateProfileRequestDto request) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));

        findUser.updateUserInfo(request.nickname(), request.profileUrl());

        userRepository.save(findUser);
        return UpdateProfileResponseDto.of(findUser);
    }

    public void poke(Long userId, UserPokeRequestDto request) {
        boolean alreadyPoke = userPokeRepository.existsByUserId(userId);
        if (alreadyPoke) {
            throw new UserException(UserExceptionType.ALREADY_POKE);
        }

        Long receiveUserId = request.userId();
        boolean isExistUser = userRepository.existsById(receiveUserId);
        if (!isExistUser) {
            throw new UserException(NOT_EXIST_USER);
        }

        UserPokeEvent userPokeEvent = new UserPokeEvent(userId, receiveUserId);
        userPokeRepository.save(userPokeEvent);

        eventPublisher.publishEvent(userPokeEvent);
    }

    // FIXME: External Service
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
    }
}
