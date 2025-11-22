package com.caixabank.authservice.exception;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ApiError> build(HttpStatus httpStatus, String message, HttpServletRequest httpServletRequest) {
        var error = new ApiError(
                LocalDateTime.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message,
                httpServletRequest.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(error);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ApiError> handleInvalidEmailException(
            InvalidEmailException ex, HttpServletRequest httpServletRequest) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), httpServletRequest);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiError> handleInvalidPasswordException(
            InvalidPasswordException ex, HttpServletRequest httpServletRequest) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), httpServletRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest httpServletRequest) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("[{} {}] Validation failed: {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), errors);
        return build(HttpStatus.BAD_REQUEST, errors, httpServletRequest);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest httpServletRequest) {
        log.warn(
                "[{} {}] Invalid login attempt: {}",
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI(),
                ex.getMessage()
        );
        return build(HttpStatus.UNAUTHORIZED, "Invalid username or password.", httpServletRequest);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException ex, HttpServletRequest httpServletRequest) {
        log.warn(
                "[{} {}] Invalid/expired JWT: {}",
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI(),
                ex.getMessage()
        );
        return build(HttpStatus.UNAUTHORIZED, "Invalid or expired token.", httpServletRequest);
    }

    @ExceptionHandler({UsernameNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<ApiError> handleNotFoundException(Exception ex, HttpServletRequest httpServletRequest) {
        log.warn(
                "[{} {}] Not found: {}",
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI(),
                ex.getMessage()
        );
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), httpServletRequest);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest httpServletRequest) {
        log.warn(
                "[{} {}] Method not allowed: {}",
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI(),
                ex.getMessage()
        );
        return build(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed.", httpServletRequest);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleConflictException(
            DataIntegrityViolationException ex, HttpServletRequest httpServletRequest) {
        log.warn(
                "[{} {}] Data integrity violation: {}",
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI(),
                ex.getMessage()
        );
        return build(HttpStatus.CONFLICT, "Email already exists.", httpServletRequest);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleGenericException(Throwable ex, HttpServletRequest httpServletRequest) {
        log.error(
                "[{} {}] Unhandled exception: {}",
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI(),
                ex.getMessage(),
                ex
        );
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error.", httpServletRequest);
    }
}
