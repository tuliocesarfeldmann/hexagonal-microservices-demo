package com.fintech.prototype.gateway.adapter.in.web;

import com.fintech.prototype.gateway.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException exception,
                                                            HttpServletRequest request) {
        String details = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .orElse("Invalid request");
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", details, request);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingHeader(MissingRequestHeaderException exception,
                                                                HttpServletRequest request) {
        return build(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Required header is missing: " + exception.getHeaderName(),
                request
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalState(IllegalStateException exception,
                                                               HttpServletRequest request) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "OPERATION_ERROR", exception.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception exception, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", exception.getMessage(), request);
    }

    private ResponseEntity<ErrorResponseDTO> build(HttpStatus status, String error, String details, HttpServletRequest request) {
        String correlationId = request.getHeader("Correlation-Id");
        return ResponseEntity
                .status(status)
                .body(new ErrorResponseDTO(error, details, correlationId, Instant.now()));
    }
}
