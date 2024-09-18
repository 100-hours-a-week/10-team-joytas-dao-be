package com.example.daobe.user.application;

import static com.example.daobe.user.exception.UserExceptionType.ALREADY_POKE;
import static com.example.daobe.user.exception.UserExceptionType.DUPLICATE_NICKNAME;
import static com.example.daobe.user.exception.UserExceptionType.NOT_EXIST_USER;

import com.example.daobe.common.response.SliceApiResponse;
import com.example.daobe.user.application.dto.UpdateProfileRequestDto;
import com.example.daobe.user.application.dto.UpdateProfileResponseDto;
import com.example.daobe.user.application.dto.UserInfoResponseDto;
import com.example.daobe.user.application.dto.UserPokeRequestDto;
import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.event.UserCreateEvent;
import com.example.daobe.user.domain.event.UserPokeEvent;
import com.example.daobe.user.domain.event.UserUpdateEvent;
import com.example.daobe.user.domain.repository.UserPokeRepository;
import com.example.daobe.user.domain.repository.UserRepository;
import com.example.daobe.user.domain.repository.UserSearchRepository;
import com.example.daobe.user.domain.repository.dto.UserSearchCondition;
import com.example.daobe.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private static final int DEFAULT_VIEW_LIMIT_SIZE = 10;
    private static final int DEFAULT_EXECUTE_LIMIT_SIZE = DEFAULT_VIEW_LIMIT_SIZE + 1;

    private final UserRepository userRepository;
    private final UserPokeRepository userPokeRepository;
    private final UserSearchRepository userSearchRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UserInfoResponseDto getUserInfoWithId(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
        return UserInfoResponseDto.of(findUser);
    }

    public SliceApiResponse<UserInfoResponseDto> searchUserByNickname(String nickname, Long cursor) {
        UserSearchCondition condition = new UserSearchCondition(nickname, cursor, DEFAULT_EXECUTE_LIMIT_SIZE);
        Slice<User> userSlice = userSearchRepository.searchByCondition(condition);
        Slice<UserInfoResponseDto> sliceUserInfo = userSlice.map(UserInfoResponseDto::of);
        return SliceApiResponse.of(sliceUserInfo, UserInfoResponseDto::userId);
    }

    public void checkValidateByNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new UserException(DUPLICATE_NICKNAME);
        }
    }

    @Transactional
    public UpdateProfileResponseDto updateProfile(Long userId, UpdateProfileRequestDto request) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
        findUser.updateUserInfo(request.nickname(), request.profileUrl());

        userRepository.save(findUser);
        eventPublisher.publishEvent(UserUpdateEvent.of(findUser));

        return UpdateProfileResponseDto.of(findUser);
    }

    public void poke(Long userId, UserPokeRequestDto request) {
        boolean alreadyPoke = userPokeRepository.existsByUserId(userId);
        if (alreadyPoke) {
            throw new UserException(ALREADY_POKE);
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

    @Transactional
    public User getOrRegisterByOAuthId(String oAuthId) {
        return userRepository.findByKakaoId(oAuthId)
                .map(this::activateIfDeleted)
                .orElseGet(() -> registerNewUserAndPublish(oAuthId));
    }

    private User activateIfDeleted(User user) {
        if (user.isDeletedUser()) {
            user.activateFirstLogin();
        }
        return user;
    }

    private User registerNewUserAndPublish(String oAuthId) {
        User newUser = User.builder().kakaoId(oAuthId).build();
        User saveUser = userRepository.save(newUser);
        eventPublisher.publishEvent(UserCreateEvent.of(saveUser));
        return saveUser;
    }

    // External Service
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
    }
}
