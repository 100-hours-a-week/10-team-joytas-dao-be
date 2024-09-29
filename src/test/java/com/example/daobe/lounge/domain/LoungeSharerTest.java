package com.example.daobe.lounge.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.daobe.common.fixtures.LoungeFixtures;
import com.example.daobe.common.fixtures.LoungeSharerFixtures;
import com.example.daobe.common.fixtures.UserFixtures;
import com.example.daobe.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoungeSharerTest {

    @Test
    @DisplayName("활성화된 라운지 공유자라면 참을 반환해야 한다.")
    void testActiveLoungeSharer() {
        // given
        User user = UserFixtures.ACTIVE_USER(1L);
        Lounge lounge = LoungeFixtures.ACTIVE_LOUNGE(1L, user);
        LoungeSharer loungeSharer = LoungeSharerFixtures.ACTIVE_LOUNGE_SHARER(user, lounge);

        // when
        boolean result = loungeSharer.isActive();

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("대기 상태의 라운지 공유자를 업데이트하면 활성 상태로 바뀌어야 한다.")
    void testUpdateStatusActive() {
        // given
        User user = UserFixtures.ACTIVE_USER(1L);
        Lounge lounge = LoungeFixtures.ACTIVE_LOUNGE(1L, user);
        LoungeSharer loungeSharer = LoungeSharerFixtures.PENDING_LOUNGE_SHARER(user, lounge);

        // when
        loungeSharer.updateStatusActive();

        // then
        assertThat(loungeSharer.isActive()).isTrue();
    }
}
