package com.example.daobe.common.utils;

public class DaoStringUtils {

    private static final String DELIMITER = "";

    public static String concatenateStrings(String... args) {
        return String.join(DELIMITER, args);
    }
}
