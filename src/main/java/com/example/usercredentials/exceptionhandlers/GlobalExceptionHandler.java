package com.example.usercredentials.exceptionhandlers;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ConstraintViolationExceptionResponseBody> handleConstraintViolationException(ConstraintViolationException exception) {
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        ConstraintViolationExceptionResponseBody exceptionResponseBody = new ConstraintViolationExceptionResponseBody(status, exception);
        return new ResponseEntity<>(exceptionResponseBody, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseBody> handleEntityNotFoundException(EntityNotFoundException exception) {
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        ExceptionResponseBody exceptionResponseBody = new ExceptionResponseBody(status, exception);
        return new ResponseEntity<>(exceptionResponseBody, status);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ExceptionResponseBody> handleEntityExistsException(EntityExistsException exception) {
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        ExceptionResponseBody exceptionResponseBody = new ExceptionResponseBody(status, exception);
        return new ResponseEntity<>(exceptionResponseBody, status);
    }
}
