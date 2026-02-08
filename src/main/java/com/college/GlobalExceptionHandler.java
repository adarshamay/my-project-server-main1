package com.college;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public String handleException (NoHandlerFoundException ex, HttpServletRequest request) {
        return "/home";
    }
}
