package com.example.daobe.common.utils;

import java.util.Objects;

public class DaoStringUtils {

    private static final String DEFAULT_STRING = "";

    /**
     * 여러 개의 인자를 받아서 순서대로 연결한 문자열을 반환합니다.
     *
     * @param args 여러 개의 인자 (어떤 타입도 가능함)
     * @return 연결된 문자열
     */
    public static String combineToString(Object... args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object arg : args) {
            stringBuilder.append(Objects.toString(arg, DEFAULT_STRING));
        }
        return stringBuilder.toString();
    }
}
