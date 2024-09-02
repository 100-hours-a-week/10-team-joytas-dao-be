package com.example.daobe.common.logging.query;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class QueryCounterAop {

    private final QueryCounter queryCounter;

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Object getConnection(ProceedingJoinPoint joinPoint) throws Throwable {
        Object connection = joinPoint.proceed();
        ProxyFactory proxyFactory = new ProxyFactory(connection);
        proxyFactory.addAdvice(new ConnectionMethodInterceptor(queryCounter));
        return proxyFactory.getProxy();
    }
}
