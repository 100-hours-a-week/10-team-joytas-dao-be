package com.example.daobe.common.fixtures;

import com.example.daobe.lounge.domain.Lounge;
import com.example.daobe.lounge.domain.LoungeSharer;
import com.example.daobe.lounge.domain.LoungeSharerStatus;
import com.example.daobe.user.domain.User;

public class LoungeSharerFixtures {

    private static final LoungeSharerStatus ACTIVE_STATUS = LoungeSharerStatus.ACTIVE;
    private static final LoungeSharerStatus PENDING_STATUS = LoungeSharerStatus.PENDING;

    public static LoungeSharer ACTIVE_LOUNGE_SHARER(User user, Lounge lounge) {
        return new LoungeSharer(null, user, lounge, ACTIVE_STATUS);
    }

    public static LoungeSharer ACTIVE_LOUNGE_SHARER(Long loungeSharerId, User user, Lounge lounge) {
        return new LoungeSharer(loungeSharerId, user, lounge, ACTIVE_STATUS);
    }

    public static LoungeSharer PENDING_LOUNGE_SHARER(User user, Lounge lounge) {
        return new LoungeSharer(null, user, lounge, PENDING_STATUS);
    }

    public static LoungeSharer PENDING_LOUNGE_SHARER(Long loungeSharerId, User user, Lounge lounge) {
        return new LoungeSharer(loungeSharerId, user, lounge, PENDING_STATUS);
    }
}
