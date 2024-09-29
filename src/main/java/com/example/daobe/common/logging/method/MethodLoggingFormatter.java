package com.example.daobe.common.logging.method;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MethodLoggingFormatter {

    private static final String CALL_METHOD = "--->";
    private static final String RETURN_METHOD = "<---";
    private static final String THROW_METHOD = "<X--";

    private final MethodLoggingContext context;

    public void callMethod(String className, String methodName) {
        context.increaseMethodDepth();
        log.info(
                "{}",
                formattedClassAndMethod(context.depthLoggingFormatted(CALL_METHOD), className, methodName)
        );
    }

    public void returnMethod(String className, String methodName) {
        log.info(
                "{}  |  TAKEN_TIME: {}ms",
                formattedClassAndMethod(context.depthLoggingFormatted(RETURN_METHOD), className, methodName),
                context.getTakenTimeMillis()
        );
        context.decreaseMethodDepth();
    }

    public void throwMethod(String className, String methodName, Throwable throwable) {
        log.info(
                "{}  |  TAKEN_TIME: {}ms  |  THROWS: {}",
                formattedClassAndMethod(context.depthLoggingFormatted(THROW_METHOD), className, methodName),
                context.getTakenTimeMillis(),
                throwable.getClass().getSimpleName()
        );
        context.decreaseMethodDepth();
    }

    private String formattedClassAndMethod(String prefix, String className, String methodName) {
        return prefix + className + "." + methodName + "()";
    }
}
