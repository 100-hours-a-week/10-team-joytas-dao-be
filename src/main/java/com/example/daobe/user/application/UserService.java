package com.example.daobe.user.application;

import static com.example.daobe.user.exception.UserExceptionType.DUPLICATE_NICKNAME;
import static com.example.daobe.user.exception.UserExceptionType.NOT_EXIST_USER;

import com.example.daobe.upload.application.UploadService;
import com.example.daobe.user.application.dto.UpdateProfileRequestDto;
import com.example.daobe.user.application.dto.UpdateProfileResponseDto;
import com.example.daobe.user.application.dto.UserInfoResponseDto;
import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.UserStatus;
import com.example.daobe.user.domain.repository.UserRepository;
import com.example.daobe.user.exception.UserException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UploadService uploadService;

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

    @Deprecated(since = "2024-08-29")
    @Transactional
    public UpdateProfileResponseDto updateProfileWithProfileImage(
            Long userId,
            String nickname,
            MultipartFile profileImage
    ) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));

        // TODO: S3 업로드 로직 분리
        String imageUrl = uploadService.uploadImage(profileImage).image();

        findUser.updateNickname(nickname);
        findUser.updateProfileUrl(imageUrl);

        userRepository.save(findUser);
        return UpdateProfileResponseDto.of(findUser);
    }

    // FIXME: External Service
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
    }
}
