package com.example.daobe.lounge.domain;

import static com.example.daobe.lounge.exception.LoungeExceptionType.LOUNGE_NAME_LENGTH_EXCEPTION;
import static com.example.daobe.lounge.exception.LoungeExceptionType.LOUNGE_NAME_REGEX_EXCEPTION;

import com.example.daobe.lounge.exception.LoungeException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class LoungeName {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 10;
    private static final String NAME_REGEX = "^(?!.*[^가-힣a-zA-Z0-9\\s])(?!.*\\s{2,}).*$";

    @Column(name = "name")
    private String name;

    public LoungeName() {
    }

    public LoungeName(String name) {
        validateLength(name.length());
        validatePattern(name);
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    private void validateLength(int length) {
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new LoungeException(LOUNGE_NAME_LENGTH_EXCEPTION);
        }
    }

    private void validatePattern(String name) {
        if (!name.matches(NAME_REGEX)) {
            throw new LoungeException(LOUNGE_NAME_REGEX_EXCEPTION);
        }
    }
}
