package com.example.online_bookstore.handler;

import com.example.online_bookstore.exception.ResourceNotFoundException;
import com.example.online_bookstore.exception.ErrorDetails;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> userNotFoundException(ResourceNotFoundException ex) {

        ErrorDetails errorModel = new ErrorDetails(0, ex.getMessage());

        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> generalError(DataIntegrityViolationException ex) {

        ErrorDetails errorModel = getErrorModel(ex);

        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }

    private ErrorDetails getErrorModel(DataIntegrityViolationException ex) {

        if(ex.getMessage().contains("OIB")) {
            return new ErrorDetails(0, "OIB is unique. Already exists customer with given OIB.");
        }

        return new ErrorDetails(0, ex.getMessage());
    }
}
