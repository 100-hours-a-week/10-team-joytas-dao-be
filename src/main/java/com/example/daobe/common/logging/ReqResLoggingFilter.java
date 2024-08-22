package com.example.daobe.common.logging;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.example.daobe.common.utils.DaoStreamUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@RequiredArgsConstructor
public class ReqResLoggingFilter extends OncePerRequestFilter {

    private static final String QUERY_COUNT_MDC_KEY = "QUERY_COUNT";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String contentType = request.getHeader(CONTENT_TYPE);
        if (LoggingMediaType.isMatchType(contentType)) {
            doFilterWithCaching(request, response, filterChain);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void doFilterWithCaching(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(response);

        // Request Latency (밀리초)
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        filterChain.doFilter(cachedRequest, cachedResponse);

        stopWatch.stop();
        logging(cachedRequest, cachedResponse, stopWatch.getTotalTimeMillis());
        cachedResponse.copyBodyToResponse();

    }

    // TODO: 리팩토링
    private void logging(
            ContentCachingRequestWrapper request,
            ContentCachingResponseWrapper response,
            long requestDuration
    ) {
        // Request & Response & QueryCount
        int status = response.getStatus();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String statusCode = HttpStatus.valueOf(status).toString();
        String totalQueryCount = getTotalQueryCount();

        // 로그 메시지 생성
        StringBuilder logMessage = new StringBuilder();

        logMessage.append("|\n| [REQUEST] (").append(method).append(") ").append(requestURI);

        getFormattedQueryString(queryString).ifPresent(v ->
                logMessage.append(v).append(queryString)
        );

        logMessage.append("\n| >> STATUS_CODE: ").append(statusCode);

        getRequestBody(request).ifPresent(v ->
                logMessage.append("\n| >> REQUEST_BODY: ").append(v)
        );

        getResponseBody(response).ifPresent(v ->
                logMessage.append("\n| >> RESPONSE_BODY: ").append(v)
        );

        logMessage.append("\n| >> TOTAL_QUERY_COUNT: ").append(totalQueryCount);

        logMessage.append("\n| >> TOTAL_LATENCY_TIME: ").append(requestDuration).append("ms");

        if (status < 500) {
            log.info(logMessage.toString());
        } else {
            log.error(logMessage.toString());
        }
    }

    private String getTotalQueryCount() {
        String queryCount = MDC.get(QUERY_COUNT_MDC_KEY);
        if (queryCount == null) {
            return "0";
        }
        return queryCount;
    }

    private Optional<String> getFormattedQueryString(String queryString) {
        if (StringUtils.hasText(queryString)) {
            return Optional.of("?");
        }
        return Optional.empty();
    }

    private Optional<String> getRequestBody(ContentCachingRequestWrapper request) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getContentAsByteArray());
        String requestBody = DaoStreamUtils.toString(inputStream);
        if (StringUtils.hasText(requestBody)) {
            return Optional.of(requestBody);
        }
        return Optional.empty();
    }

    private Optional<String> getResponseBody(ContentCachingResponseWrapper response) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getContentAsByteArray());
        String responseBody = DaoStreamUtils.toString(inputStream);
        if (StringUtils.hasText(responseBody)) {
            return Optional.of(responseBody);
        }
        return Optional.empty();
    }
}
