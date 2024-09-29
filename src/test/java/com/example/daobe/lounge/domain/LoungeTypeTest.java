package com.example.daobe.lounge.domain;

import static com.example.daobe.lounge.exception.LoungeExceptionType.INVALID_LOUNGE_TYPE_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.daobe.lounge.exception.LoungeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LoungeTypeTest {

    @ParameterizedTest
    @ValueSource(strings = {"L0001", "L0002", "L0003"})
    @DisplayName("유효한 라운지 타입명을 통해 라운지 타입을 반환 해야 한다.")
    void testFindLoungeTypeByName(String type) {
        // given & when
        LoungeType loungeType = LoungeType.from(type);

        // then
        assertThat(loungeType).isNotNull();
    }

    @Test
    @DisplayName("null을 통해 문자열을 전달하면 예외가 발생 해야 한다.")
    void tsetFindLoungeTypeByNull() {
        // when, then
        assertThatThrownBy(() -> LoungeType.from(null))
                .isInstanceOf(LoungeException.class)
                .hasMessage(INVALID_LOUNGE_TYPE_EXCEPTION.message());
    }

    @Test
    @DisplayName("빈 문자열을 전달하면 예외가 발생 해야 한다.")
    void tsetFindLoungeTypeByBlank() {
        // when, then
        assertThatThrownBy(() -> LoungeType.from(""))
                .isInstanceOf(LoungeException.class)
                .hasMessage(INVALID_LOUNGE_TYPE_EXCEPTION.message());
    }

    @ParameterizedTest
    @ValueSource(strings = {"l0001", "L0001"})
    @DisplayName("대소문자 구분을 테스트한다.")
    void testCaseSensitiveLoungeType(String type) {
        // when
        LoungeType loungeType = LoungeType.from(type);

        // when, then
        assertThat(loungeType).isNotNull();
    }
}