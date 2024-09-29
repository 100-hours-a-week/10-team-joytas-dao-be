package com.example.daobe.common.fixtures;

import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.LoungeStatus;
import com.example.daobe.lounge.domain.LoungeType;
import com.example.daobe.user.domain.User;

public class LoungeFixtures {

    private static final String FAKE_LOUNGE_NAME = "Test Lounge";
    private static final LoungeType FAKE_LOUNGE_TYPE = LoungeType.L0001;
    private static final LoungeStatus ACTIVE_STATUS = LoungeStatus.ACTIVE;
    private static final LoungeStatus DELETED_STATUS = LoungeStatus.DELETED;

    public static Lounge ACTIVE_LOUNGE() {
        return new Lounge(null, null, FAKE_LOUNGE_NAME, FAKE_LOUNGE_TYPE, ACTIVE_STATUS);
    }

    public static Lounge ACTIVE_LOUNGE(User user) {
        return new Lounge(null, user, FAKE_LOUNGE_NAME, FAKE_LOUNGE_TYPE, ACTIVE_STATUS);
    }

    public static Lounge ACTIVE_LOUNGE(Long loungeId, User user) {
        return new Lounge(loungeId, user, FAKE_LOUNGE_NAME, FAKE_LOUNGE_TYPE, ACTIVE_STATUS);
    }

    public static Lounge DELETED_LOUNGE() {
        return new Lounge(null, null, FAKE_LOUNGE_NAME, FAKE_LOUNGE_TYPE, DELETED_STATUS);
    }

    public static Lounge DELETED_LOUNGE(User user) {
        return new Lounge(null, user, FAKE_LOUNGE_NAME, FAKE_LOUNGE_TYPE, DELETED_STATUS);
    }

    public static Lounge DELETED_LOUNGE(Long loungeId, User user) {
        return new Lounge(loungeId, user, FAKE_LOUNGE_NAME, FAKE_LOUNGE_TYPE, DELETED_STATUS);
    }
}
