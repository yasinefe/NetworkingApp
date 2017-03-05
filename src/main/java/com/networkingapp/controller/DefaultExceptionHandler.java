package com.networkingapp.controller;

import com.networkingapp.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler
    private ResponseEntity handle(UserNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

}
