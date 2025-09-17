package com.modding.mp.adapter.out.error;

import java.util.List;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.modding.mp.adapter.in.web.response.StandardError;
import com.modding.mp.adapter.in.web.response.StandardError.FieldError;
import com.modding.mp.application.usecase.exceptions.BadRequestException;
import com.modding.mp.application.usecase.exceptions.EmailAlreadyUsedException;
import com.modding.mp.application.usecase.exceptions.UnauthorizedException;
import com.modding.mp.application.usecase.exceptions.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<FieldError> details = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> new FieldError(err.getField(), err.getDefaultMessage()))
            .toList();

        var body = StandardError.of(
            "Bad Request",
            "VALIDATION_ERROR",
            BAD_REQUEST.value(),
            req.getRequestURI(),
            "The request contains invalid fields.",
            details
        );
        return ResponseEntity.status(BAD_REQUEST).body(body);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        var body = StandardError.of(
            "Bad Request",
            "BAD_REQUEST",
            BAD_REQUEST.value(),
            req.getRequestURI(),
            ex.getMessage(),
            null
        );
        return ResponseEntity.status(BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardError> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        var details = ex.getConstraintViolations().stream()
            .map(v -> new FieldError(v.getPropertyPath().toString(), v.getMessage()))
            .toList();

        var body = StandardError.of(
            "Bad Request",
            "CONSTRAINT_VIOLATION",
            BAD_REQUEST.value(),
            req.getRequestURI(),
            "Invalid request parameters.",
            details
        );
        return ResponseEntity.status(BAD_REQUEST).body(body);
    }

     @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        var body = StandardError.of(
            "Bad Request",
            "MALFORMED_JSON",
            BAD_REQUEST.value(),
            req.getRequestURI(),
            "Invalid or malformed request body.",
            null
        );
        return ResponseEntity.status(BAD_REQUEST).body(body);
    }

     @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        var body = StandardError.of("Forbidden", "ACCESS_DENIED", FORBIDDEN.value(), req.getRequestURI(),
                "You do not have permissions for this.", null);
        return ResponseEntity.status(FORBIDDEN).body(body);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardError> handleUnauthorized(UnauthorizedException ex, HttpServletRequest req) {
        var body = StandardError.of("Unauthorized", "INVALID_CREDENTIALS", 401,
            req.getRequestURI(), ex.getMessage(), null);
        return ResponseEntity.status(401).body(body);
    }

    // 404
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandardError> handleUserNotFound(UserNotFoundException ex, HttpServletRequest req) {
        var body = StandardError.of("Not Found", "USER_NOT_FOUND", NOT_FOUND.value(), req.getRequestURI(),
                ex.getMessage(), null);
        return ResponseEntity.status(NOT_FOUND).body(body);
    }

    // 409
    @ExceptionHandler({ EmailAlreadyUsedException.class, DataIntegrityViolationException.class })
    public ResponseEntity<StandardError> handleConflict(Exception ex, HttpServletRequest req) {
        var body = StandardError.of("Conflict", "CONFLICT", CONFLICT.value(), req.getRequestURI(),
                "The operation conflicts with existing data.", null);
        return ResponseEntity.status(CONFLICT).body(body);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleUnexpected(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error processing {} {}", req.getMethod(), req.getRequestURI(), ex);

        var body = StandardError.of("Internal Server Error", "UNEXPECTED_ERROR", INTERNAL_SERVER_ERROR.value(),
                req.getRequestURI(), "An internal error has occurred.", null);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(body);
    }
}
