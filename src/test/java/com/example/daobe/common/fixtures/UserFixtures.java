package com.example.daobe.common.fixtures;

import com.example.daobe.user.domain.User;
import com.example.daobe.user.domain.UserStatus;
import java.util.Random;

public class UserFixtures {

    private static final String FAKE_KAKAO_ID = "1234567890";
    private static final String FAKE_NICKNAME = "hong";
    private static final String FAKE_PROFILE_URL = "https://joytas.io/hong-profile-url";

    private static final UserStatus ACTIVE_STATUS = UserStatus.ACTIVE;

    private static final Random RANDOM = new Random();

    public static User FIRST_LOGIN_USER() {
        return new User(FAKE_KAKAO_ID);
    }

    public static User ACTIVE_USER() {
        return new User(null, generateUniqueKakaoId(), FAKE_NICKNAME, FAKE_PROFILE_URL, ACTIVE_STATUS);
    }

    public static User ACTIVE_USER(Long userId) {
        return new User(userId, generateUniqueKakaoId(), FAKE_NICKNAME, FAKE_PROFILE_URL, ACTIVE_STATUS);
    }

    public static User ACTIVE_USER(Long userId, String nickname) {
        return new User(userId, generateUniqueKakaoId(), nickname, FAKE_PROFILE_URL, ACTIVE_STATUS);
    }

    private static String generateUniqueKakaoId() {
        long randomNumber = 1_000_000_000L + (long) (RANDOM.nextDouble() * 9_000_000_000L);
        return String.valueOf(randomNumber);
    }
}
