package com.example.daobe.lounge.domain;

import static com.example.daobe.lounge.exception.LoungeExceptionType.LOUNGE_NAME_LENGTH_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.LOUNGE_NAME_REGEX_EXCEPTION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.example.daobe.lounge.exception.LoungeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LoungeNameTest {

    @ParameterizedTest
    @ValueSource(strings = {"안녕하세요", "Lounge1", "한글 영어 1"})
    @DisplayName("올바른 라운지 이름인 경우 예외가 발생하지 않아야 한다.")
    void testValidLoungeName(String validName) {
        assertThatNoException().isThrownBy(() -> new LoungeName(validName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"안"})
    @DisplayName("라운지 이름이 2자 미만인 경우 예외가 발생해야 한다.")
    void testTooShortLoungeName(String shortName) {
        assertThatThrownBy(() -> new LoungeName(shortName))
                .isInstanceOf(LoungeException.class)
                .hasMessage(LOUNGE_NAME_LENGTH_EXCEPTION.message());
    }

    @ParameterizedTest
    @ValueSource(strings = {"안녕하세요 안녕하세요"})
    @DisplayName("라운지 이름이 10자를 초과하는 경우 예외가 발생해야 한다.")
    void testTooLongLoungeName(String longName) {
        assertThatThrownBy(() -> new LoungeName(longName))
                .isInstanceOf(LoungeException.class)
                .hasMessage(LOUNGE_NAME_LENGTH_EXCEPTION.message());
    }

    @ParameterizedTest
    @ValueSource(strings = {"new@Lounge", "Lounge!", "라운지#1"})
    @DisplayName("라운지 이름에 특수문자가 포함된 경우 예외가 발생해야 한다.")
    void testLoungeNameWithSpecialCharacters(String invalidName) {
        assertThatThrownBy(() -> new LoungeName(invalidName))
                .isInstanceOf(LoungeException.class)
                .hasMessage(LOUNGE_NAME_REGEX_EXCEPTION.message());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Lou  nge", "라운지  이름"})
    @DisplayName("라운지 이름에 연속된 공백이 포함된 경우 예외가 발생해야 한다.")
    void testLoungeNameWithConsecutiveSpaces(String nameWithSpaces) {
        assertThatThrownBy(() -> new LoungeName(nameWithSpaces))
                .isInstanceOf(LoungeException.class)
                .hasMessage(LOUNGE_NAME_REGEX_EXCEPTION.message());
    }
}
