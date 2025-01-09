package org.example.application.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StatusMappingException.class)
    public ResponseEntity<String> handleStatusMappingException(StatusMappingException ex) {
        return ResponseEntity.badRequest().body("Invalid status provided: " + ex.getMessage());
    }
}