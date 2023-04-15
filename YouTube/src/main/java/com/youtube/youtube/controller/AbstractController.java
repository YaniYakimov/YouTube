package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.ErrorDTO;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractController {
    protected static final String LOGGED = "LOGGED";
    protected static final String LOGGED_ID = "LOGGED_ID";

    protected int getLoggedId (HttpSession s){
        if(s.getAttribute(LOGGED) == null){
            throw new UnauthorizedException("You have to login first.");
        }
        return (int) s.getAttribute(LOGGED_ID);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return generateErrorDTO(errors, HttpStatus.BAD_REQUEST);
    }

    private ErrorDTO generateErrorDTO(Object object, HttpStatus status) {
        return ErrorDTO.builder()
                .msg(object)
                .time(LocalDateTime.now())
                .status(status.value())
                .build();
    }
}
