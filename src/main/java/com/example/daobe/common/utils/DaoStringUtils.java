package com.example.daobe.common.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

public class DaoStringUtils {

    private static final String DEFAULT_STRING = "";

    /**
     * 여러 개의 인자를 받아서 순서대로 연결한 문자열을 반환합니다.
     *
     * @param args 여러 개의 인자 (어떤 타입도 가능함)
     * @return 연결된 문자열
     * @author JiHonkim98
     */
    public static String combineToString(Object... args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object arg : args) {
            stringBuilder.append(Objects.toString(arg, DEFAULT_STRING));
        }
        return stringBuilder.toString();
    }

    /**
     * UTF-8 인코딩을 통해서 바이트 배열을 문자열로 변환합니다.
     * <p>변환중 `IOException`이 발생하는 경우 빈 문자열 반환합니다.</p>
     *
     * @param byteArray 문자열로 변환할 바이트
     * @return 변환된 문자열
     * @author JiHonkim98
     */
    public static String byteArrayToString(byte[] byteArray) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        try {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            return DEFAULT_STRING;
        }
    }

    /**
     * 인자로 들어온 문자열이 빈값인지 확인합니다.
     *
     * @param stringValue 검증할 문자열
     * @return null safe 한 문자열
     * @author JiHonkim98
     */
    public static Optional<String> optionalOfNonEmpty(String stringValue) {
        if (StringUtils.hasText(stringValue)) {
            return Optional.of(stringValue);
        }
        return Optional.empty();
    }
}
