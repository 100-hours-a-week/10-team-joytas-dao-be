package com.example.daobe.common.logging;

import java.util.stream.Stream;
import org.springframework.http.MediaType;

public enum LoggingMediaType {
    APPLICATION_JSON(MediaType.APPLICATION_JSON_VALUE),
    MULTIPART_FORM_DATA(MediaType.MULTIPART_FORM_DATA_VALUE),
    ALL(MediaType.ALL_VALUE),
    ;

    private final String mediaType;

    LoggingMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public static boolean isMatchType(String mediaType) {
        return Stream.of(LoggingMediaType.values())
                .anyMatch(type -> type.isContainEnumType(mediaType));
    }

    private boolean isContainEnumType(String mediaType) {
        return mediaType != null &&
                mediaType.toLowerCase().startsWith(this.mediaType.toLowerCase());
    }
}
