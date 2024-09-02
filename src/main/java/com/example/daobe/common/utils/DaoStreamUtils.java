package com.example.daobe.common.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.util.StreamUtils;

public class DaoStreamUtils {

    /**
     * UTF-8 인코딩을 통해서 `ByteArrayInputStream`를 문자열로 변환합니다.
     *
     * <p>`IOException`이 발생하는 경우 빈 문자열 반환합니다.</p>
     *
     * @param inputStream 변환할 바이트 스트림
     * @return 변환된 문자열
     * @author JiHonkim98
     */
    public static String toString(ByteArrayInputStream inputStream) {
        try {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            return "";
        }
    }
}
