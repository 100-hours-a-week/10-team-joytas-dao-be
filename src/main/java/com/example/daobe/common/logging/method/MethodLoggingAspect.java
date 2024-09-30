package com.example.daobe.common.logging.method;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
@RequiredArgsConstructor
public class MethodLoggingAspect {

    private static final String PROXY_CLASS_PREFIX = "Proxy";

    private final MethodLoggingFormatter methodLoggingFormatter;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restController() {
    }

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void service() {
    }

    @Pointcut("execution(* com.example.daobe..*Repository+.*(..))")
    public void repository() {
    }

    @Around("restController() || service() || repository()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        if (isWithinRequestScope()) {
            String className = extractClassName(joinPoint);
            String methodName = joinPoint.getSignature().getName();
            methodLoggingFormatter.callMethod(className, methodName);

            Object result = proceedResult(joinPoint, className, methodName);

            methodLoggingFormatter.returnMethod(className, methodName);
            return result;
        }
        return joinPoint.proceed();
    }

    private Object proceedResult(ProceedingJoinPoint joinPoint, String className, String methodName) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            methodLoggingFormatter.throwMethod(className, methodName, ex);
            throw ex;
        }
    }

    private boolean isWithinRequestScope() {
        return Objects.nonNull(RequestContextHolder.getRequestAttributes());
    }

    private String extractClassName(ProceedingJoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        return getClassName(targetClass);
    }

    private String getClassName(Class<?> clazz) {
        if (isProxyClassAndHasInterface(clazz)) {
            return clazz.getInterfaces()[0].getSimpleName();  // 싱글톤 클래스 이름 가져오기
        }
        return clazz.getSimpleName();
    }

    private boolean isProxyClassAndHasInterface(Class<?> clazz) {
        return clazz.getSimpleName().contains(PROXY_CLASS_PREFIX) &&
                clazz.getInterfaces().length > 0;
    }
}
