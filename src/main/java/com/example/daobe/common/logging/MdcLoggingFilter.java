package com.example.daobe.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

public class MdcLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "request_id";
    private static final String X_REQUEST_ID = "X-Request-ID";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        MDC.put(REQUEST_ID, getRequestIdOrGeneratedUUID(request));

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private String getRequestIdOrGeneratedUUID(HttpServletRequest request) {
        String requestId = request.getHeader(X_REQUEST_ID);
        return Objects.requireNonNullElse(requestId, generatedUUID());
    }

    private String generatedUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
