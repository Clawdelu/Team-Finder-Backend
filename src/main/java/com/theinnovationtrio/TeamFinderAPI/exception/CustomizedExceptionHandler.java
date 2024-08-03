package com.theinnovationtrio.TeamFinderAPI.exception;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("NullableProblems")
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, @NotNull HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, Object> errors = new HashMap<>();
        errors.put("totalErrors", ex.getErrorCount());

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getFieldErrors().forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        errors.put("errors", fieldErrors);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }
}
