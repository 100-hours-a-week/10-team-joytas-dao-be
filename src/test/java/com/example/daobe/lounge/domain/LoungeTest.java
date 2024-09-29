package com.example.daobe.lounge.domain;

import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_OWNER_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.NOT_ACTIVE_LOUNGE_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.NOT_ALLOW_LOUNGE_WITHDRAW_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.example.daobe.common.fixtures.LoungeFixtures;
import com.example.daobe.common.fixtures.UserFixtures;
import com.example.daobe.lounge.exception.LoungeException;
import com.example.daobe.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoungeTest {

    User ownerUser;
    User otherUser;

    @BeforeEach
    void setUp() {
        ownerUser = UserFixtures.ACTIVE_USER(1L);
        otherUser = UserFixtures.ACTIVE_USER(2L);
    }

    @Test
    @DisplayName("라운지 생성자가 라운지 삭제 시 DELETED 상태로 바뀌어야 한다.")
    void testDeleteLoungeByCreatorTest() {
        // given
        Lounge lounge = LoungeFixtures.ACTIVE_LOUNGE(1L, ownerUser);

        // when
        lounge.softDelete(ownerUser.getId());

        // then
        assertThat(lounge.getStatus()).isEqualTo(LoungeStatus.DELETED);
    }

    @Test
    @DisplayName("라운지 생성자가 아닌 사용자가 라운지 삭제 시 예외가 발생 해야 한다.")
    void testDeleteLoungeByNotCreatorTest() {
        // given
        Lounge lounge = LoungeFixtures.ACTIVE_LOUNGE(1L, ownerUser);

        // when, then
        assertThatThrownBy(() -> lounge.softDelete(otherUser.getId()))
                .isInstanceOf(LoungeException.class)
                .hasMessage(INVALID_LOUNGE_OWNER_EXCEPTION.message());
    }

    @Test
    @DisplayName("null 사용자가 라운지 삭제 시 예외가 발생 해야 한다.")
    void testDeleteLoungeWithNullUser() {
        // given
        Lounge lounge = LoungeFixtures.ACTIVE_LOUNGE(1L, ownerUser);

        // when, then
        assertThatThrownBy(() -> lounge.softDelete(null))
                .isInstanceOf(LoungeException.class);
    }

    @Test
    @DisplayName("이미 삭제된 라운지 삭제 시 예외가 발생 해야 한다.")
    void testDeleteLoungeByDeletedTest() {
        // given
        Lounge lounge = LoungeFixtures.DELETED_LOUNGE(1L, ownerUser);

        // when, then
        assertThatThrownBy(() -> lounge.softDelete(ownerUser.getId()))
                .isInstanceOf(LoungeException.class)
                .hasMessage(NOT_ACTIVE_LOUNGE_EXCEPTION.message());
    }

    @Test
    @DisplayName("라운지 생성자가 아닌 사용자는 라운지에서 탈퇴할 수 있어야 한다.")
    void testNonOwnerCanWithdraw() {
        // given
        Lounge lounge = LoungeFixtures.ACTIVE_LOUNGE(1L, ownerUser);

        // when, then
        assertDoesNotThrow(() -> lounge.isPossibleToWithdrawOrThrow(otherUser.getId()));
    }

    @Test
    @DisplayName("라운지 생성자가 라운지 탈퇴시 예외가 발생 해야 한다.")
    void testOwnerCanNotWithdraw() {
        // given
        Lounge lounge = LoungeFixtures.ACTIVE_LOUNGE(1L, ownerUser);

        // when, then
        assertThatThrownBy(() -> lounge.isPossibleToWithdrawOrThrow(ownerUser.getId()))
                .isInstanceOf(LoungeException.class)
                .hasMessage(NOT_ALLOW_LOUNGE_WITHDRAW_EXCEPTION.message());
    }

    @Test
    @DisplayName("삭제된 라운지에서 탈퇴시 예외가 발생 해야 한다.")
    void testDeletedLoungeCanWithDraw() {
        // given
        Lounge lounge = LoungeFixtures.DELETED_LOUNGE(1L, ownerUser);

        // when, then
        assertThatThrownBy(() -> lounge.isPossibleToWithdrawOrThrow(otherUser.getId()))
                .isInstanceOf(LoungeException.class)
                .hasMessage(NOT_ACTIVE_LOUNGE_EXCEPTION.message());
    }
}
