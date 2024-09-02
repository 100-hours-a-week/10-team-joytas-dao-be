package com.example.daobe.common.logging.query;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueryCountInterceptor implements HandlerInterceptor {

    private static final String QUERY_COUNT_MDC_KEY = "QUERY_COUNT";

    private final QueryCounter queryCounter;

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) throws Exception {
        int queryCount = queryCounter.getQueryCount();
        MDC.put(QUERY_COUNT_MDC_KEY, String.valueOf(queryCount));
    }
}
