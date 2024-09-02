package com.example.daobe.common.logging.query;

import java.sql.PreparedStatement;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

@RequiredArgsConstructor
public class ConnectionMethodInterceptor implements MethodInterceptor {

    private final QueryCounter queryCounter;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();

        if (result instanceof PreparedStatement preparedStatement) {
            // dynamic proxy 적용
            ProxyFactory proxyFactory = new ProxyFactory(preparedStatement);
            proxyFactory.addAdvice(new QueryMethodInterceptor(queryCounter));
            return proxyFactory.getProxy();
        }

        return result;
    }
}
