package ru.jetlabs.ts.clientwebbff.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {

        String bodyOfResponse = ex.getMessage() + "\n" + request.toString();

        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}