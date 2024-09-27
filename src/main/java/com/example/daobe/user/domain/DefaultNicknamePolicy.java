package com.example.daobe.user.domain;

import java.util.UUID;

public class DefaultNicknamePolicy {

    private static final int RANDOM_LENGTH = 8;
    private static final String PREFIX = "DAO-";

    public static String generatedRandomString() {
        String randomString = generatedSubStringUUID();
        return PREFIX + randomString;
    }

    private static String generatedSubStringUUID() {
        return UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, DefaultNicknamePolicy.RANDOM_LENGTH);
    }
}
