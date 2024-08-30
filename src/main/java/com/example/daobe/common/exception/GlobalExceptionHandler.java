package com.example.daobe.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String SERVER_ERROR_MESSAGE = "INTERNAL_SERVER_ERROR";
    private static final String MISSING_REQUEST_PARAMETER = "MISSING_REQUEST_PARAMETER";

    // Custom Exception 응답
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleException(BaseException ex) {
        log.warn(ex.getMessage(), ex);

        BaseExceptionType type = ex.getType();
        return ResponseEntity.status(type.status())
                .body(new ExceptionResponse(type.status().value(), ex.getMessage()));
    }

    // Multipart Exception 응답
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ExceptionResponse> handleException(MultipartException ex) {
        log.warn(ex.getMessage(), ex);

        return ResponseEntity.status(BAD_REQUEST)
                .body(new ExceptionResponse(BAD_REQUEST.value(), ex.getMessage()));
    }

    // Custom Exception 이외의 예외 응답
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);

        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(INTERNAL_SERVER_ERROR.value(), SERVER_ERROR_MESSAGE));
    }

    // Request Parameter 예외 응답
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.warn(ex.getMessage(), ex);

        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(BAD_REQUEST.value(), MISSING_REQUEST_PARAMETER));
    }

    // No Resource Found 예외 응답
    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.warn(ex.getMessage(), ex);

        return ResponseEntity.status(METHOD_NOT_ALLOWED)
                .body(new ExceptionResponse(METHOD_NOT_ALLOWED.value(), METHOD_NOT_ALLOWED.name()));
    }
}
