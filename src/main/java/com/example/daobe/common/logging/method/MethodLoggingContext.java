package com.example.daobe.common.logging.method;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class MethodLoggingContext {

    private static final String BAR_FORMAT = "|";
    private static final String DEPTH_BAR_FORMAT = "|    ";

    private int methodDepth;
    private final long startTime;

    public MethodLoggingContext() {
        this.methodDepth = 0;
        this.startTime = System.currentTimeMillis();
    }

    public void increaseMethodDepth() {
        this.methodDepth++;
    }

    public void decreaseMethodDepth() {
        this.methodDepth--;
    }

    public String depthLoggingFormatted(String depthPrefixString) {
        if (methodDepth == 1) {
            return BAR_FORMAT + depthPrefixString;
        }
        return DEPTH_BAR_FORMAT.repeat(methodDepth - 1) + BAR_FORMAT + depthPrefixString;
    }

    public long getTakenTimeMillis() {
        return System.currentTimeMillis() - startTime;
    }
}
