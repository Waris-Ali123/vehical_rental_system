package com.capstone1.vehical_rental_system.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static LocalDateTime time;

    // Handle validation exceptions for @Valid annotated DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        time = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        response.put("timestamp", time);
        response.put("status", HttpStatus.BAD_REQUEST.toString());
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle validation exceptions for @RequestParam or @PathVariable
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        time = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });

        response.put("timestamp", time);
        response.put("status", HttpStatus.BAD_REQUEST.toString());
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle malformed JSON requests
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        time = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", time);
        response.put("status", HttpStatus.BAD_REQUEST.toString());
        response.put("error", "Malformed JSON request");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle missing required request parameters
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        time = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", time);
        response.put("status", HttpStatus.BAD_REQUEST.toString());
        response.put("error", "Missing required request parameter");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle missing required path variables
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Map<String, Object>> handleMissingPathVariableException(MissingPathVariableException ex) {
        time = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", time);
        response.put("status", HttpStatus.BAD_REQUEST.toString());
        response.put("error", "Missing required path variable");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle unsupported HTTP methods
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        time = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", time);
        response.put("status", HttpStatus.METHOD_NOT_ALLOWED.toString());
        response.put("error", "HTTP method not supported");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    // Handle access denied exceptions
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        time = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", time);
        response.put("status", HttpStatus.FORBIDDEN.toString());
        response.put("error", "Access denied");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // Handle database constraint violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        time = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", time);
        response.put("status", HttpStatus.CONFLICT.toString());
        response.put("error", "Data integrity violation");
        response.put("message", ex.getRootCause().getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Handle resource not found exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        time = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", time);
        response.put("status", HttpStatus.NOT_FOUND.toString());
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        time = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", time);
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        response.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}