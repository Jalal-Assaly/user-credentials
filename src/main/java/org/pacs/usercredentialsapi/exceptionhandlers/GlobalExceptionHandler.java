package org.pacs.usercredentialsapi.exceptionhandlers;

import org.pacs.usercredentialsapi.exceptionhandlers.responsebodies.ConstraintViolationExceptionResponseBody;
import org.pacs.usercredentialsapi.exceptionhandlers.responsebodies.EntityNotFoundExceptionResponseBody;
import org.pacs.usercredentialsapi.exceptionhandlers.responsebodies.ValidationExceptionBody;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
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
    public ResponseEntity<EntityNotFoundExceptionResponseBody> handleEntityNotFoundException(EntityNotFoundException exception) {
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        EntityNotFoundExceptionResponseBody entityNotFoundExceptionResponseBody = new EntityNotFoundExceptionResponseBody(status, exception);
        return new ResponseEntity<>(entityNotFoundExceptionResponseBody, status);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<EntityNotFoundExceptionResponseBody> handleEntityExistsException(EntityExistsException exception) {
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        EntityNotFoundExceptionResponseBody entityNotFoundExceptionResponseBody = new EntityNotFoundExceptionResponseBody(status, exception);
        return new ResponseEntity<>(entityNotFoundExceptionResponseBody, status);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationExceptionBody> handleValidationException(ValidationException exception) {
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        ValidationExceptionBody body = new ValidationExceptionBody(status,exception);
        return new ResponseEntity<>(body,status);
    }
}
