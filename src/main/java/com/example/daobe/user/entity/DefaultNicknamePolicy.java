package com.example.daobe.user.entity;

import java.util.UUID;

public class DefaultNicknamePolicy {

    private static final String PREFIX = "DAO-";
    private static final int DEFAULT_LENGTH = 8;

    public static String generatedRandomString(int maxLength) {
        return generatedSubStringUUID(maxLength);
    }

    public static String generatedRandomString() {
        return generatedSubStringUUID(DEFAULT_LENGTH);
    }

    private static String generatedSubStringUUID(int length) {
        String substringUUID = UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, length);
        return PREFIX + substringUUID;
    }
}
