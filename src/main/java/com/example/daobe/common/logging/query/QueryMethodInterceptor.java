package com.example.daobe.common.logging.query;

import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;

@RequiredArgsConstructor
public class QueryMethodInterceptor implements MethodInterceptor {

    private static final Set<String> PREPARED_STATEMENTS = Set.of("execute", "executeQuery", "executeUpdate");

    private final QueryCounter queryCounter;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (isExecuteQuery(invocation) && isRequest()) {
            queryCounter.increase();
        }
        return invocation.proceed();  // 타겟 로직 수행
    }

    private boolean isExecuteQuery(MethodInvocation invocation) {
        String methodName = invocation.getMethod().getName();
        return PREPARED_STATEMENTS.contains(methodName);
    }

    private boolean isRequest() {
        return Objects.nonNull(RequestContextHolder.getRequestAttributes());
    }
}
