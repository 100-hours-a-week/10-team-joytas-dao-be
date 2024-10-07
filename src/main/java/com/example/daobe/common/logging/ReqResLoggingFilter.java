package com.example.daobe.common.logging;

import com.example.daobe.common.utils.DaoStringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@RequiredArgsConstructor
public class ReqResLoggingFilter extends OncePerRequestFilter {

    private static final String QUERY_STRING_FORMAT = "?";
    private static final String DEFAULT_QUERY_COUNT = "0";
    private static final String QUERY_COUNT_MDC_KEY = "QUERY_COUNT";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        return Objects.equals(acceptHeader, MediaType.TEXT_EVENT_STREAM_VALUE);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(response);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        filterChain.doFilter(cachedRequest, cachedResponse);

        stopWatch.stop();
        logRequestAndResponse(cachedRequest, cachedResponse, stopWatch.getTotalTimeMillis());
        cachedResponse.copyBodyToResponse();
    }

    private void logRequestAndResponse(
            ContentCachingRequestWrapper request,
            ContentCachingResponseWrapper response,
            long requestDuration
    ) {
        int status = response.getStatus();
        String method = request.getMethod();
        String requestUrl = getRequestUrl(request.getRequestURI(), request.getQueryString());
        String statusCode = HttpStatus.valueOf(status).toString();
        String remoteAddr = request.getRemoteAddr();
        String totalQueryCount = getTotalQueryCount();

        StringBuilder logMessage = new StringBuilder();

        logMessage.append("|\n| [REQUEST] (").append(method).append(") ").append(requestUrl);
        logMessage.append("\n| >> STATUS_CODE: ").append(statusCode);
        logMessage.append("\n| >> IPV4_ADDRESS: ").append(remoteAddr);

        getRequestBody(request).ifPresent(v -> logMessage.append("\n| >> REQUEST_BODY: ").append(v));
        getResponseBody(response).ifPresent(v -> logMessage.append("\n| >> RESPONSE_BODY: ").append(v));

        logMessage.append("\n| >> TOTAL_QUERY_COUNT: ").append(totalQueryCount);
        logMessage.append("\n| >> TOTAL_LATENCY_TIME: ").append(requestDuration).append("ms");

        logByStatus(status, logMessage.toString());
    }

    private String getTotalQueryCount() {
        String queryCount = MDC.get(QUERY_COUNT_MDC_KEY);
        if (StringUtils.hasText(queryCount)) {
            return queryCount;
        }
        return DEFAULT_QUERY_COUNT;
    }

    private String getRequestUrl(String requestURI, String queryString) {
        if (StringUtils.hasText(queryString)) {
            return requestURI + QUERY_STRING_FORMAT + queryString;
        }
        return requestURI;
    }

    private Optional<String> getRequestBody(ContentCachingRequestWrapper request) {
        byte[] byteArray = request.getContentAsByteArray();
        String requestBody = DaoStringUtils.byteArrayToString(byteArray);
        return DaoStringUtils.optionalOfNonEmpty(requestBody);
    }

    private Optional<String> getResponseBody(ContentCachingResponseWrapper response) {
        byte[] byteArray = response.getContentAsByteArray();
        String responseBody = DaoStringUtils.byteArrayToString(byteArray);
        return DaoStringUtils.optionalOfNonEmpty(responseBody);
    }

    private void logByStatus(int status, String logMessage) {
        if (status < 500) {
            log.info(logMessage);
        } else {
            log.error(logMessage);
        }
    }
}
