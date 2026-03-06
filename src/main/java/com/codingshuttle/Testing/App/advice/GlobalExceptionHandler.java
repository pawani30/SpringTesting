package com.codingshuttle.Testing.App.advice;

import com.codingshuttle.Testing.App.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //Tells the spring boot that all the exceptions are handles in this file

public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception){
        //return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.NOT_FOUND); //We can also return the error message from here
    return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRunTimeException(RuntimeException ex){
        return ResponseEntity.internalServerError().build();
    }
}

//While creating package advices.GlobalExceptionHandler it will automatically create a Java class inside the package