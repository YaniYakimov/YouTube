package com.youtube.youtube.controller;

import com.youtube.youtube.JwtUtil;
import com.youtube.youtube.model.DTOs.ErrorDTO;
import com.youtube.youtube.model.exceptions.BadRequestException;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Setter
public abstract class AbstractController {
    protected static final String LOGGED = "LOGGED";
    protected static final String LOGGED_ID = "LOGGED_ID";
    public static final String YOU_HAVE_TO_LOG_IN_FIRST = "You have to logIn first!";
    @Autowired
    protected JwtUtil jwtUtils;

    protected int getLoggedId (HttpSession s){
        if(s.getAttribute(LOGGED) == null){
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        return (int) s.getAttribute(LOGGED_ID);
    }
    protected int getUserId(String authHeader) {
        int userId = 0;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtils.validateToken(token)) {
                userId = jwtUtils.getUserIdFromToken(token);
            }
        }
        if(userId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        return userId;
    }
    protected void addToBlacklist(String authHeader, String body) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtils.validateToken(token)) {
                jwtUtils.addToBlacklist(token);
                ResponseEntity.ok(body);
            }
        }
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(Exception e){
        e.printStackTrace();
        ErrorDTO errorDTO= generateErrorDTO(e, HttpStatus.BAD_REQUEST);
        errorDTO.setMsg(e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleUnauthorized(Exception e){
        e.printStackTrace();
        ErrorDTO errorDTO= generateErrorDTO(e, HttpStatus.UNAUTHORIZED);
        errorDTO.setMsg(e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(Exception e){
        e.printStackTrace();
        ErrorDTO errorDTO= generateErrorDTO(e, HttpStatus.NOT_FOUND);
        errorDTO.setMsg(e.getMessage());
        return errorDTO;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return generateErrorDTO(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleRest(Exception e){
        e.printStackTrace();
        ErrorDTO errorDTO= generateErrorDTO(e, HttpStatus.INTERNAL_SERVER_ERROR);
        errorDTO.setMsg(e.getMessage());
        return errorDTO;
    }

    private ErrorDTO generateErrorDTO(Object object, HttpStatus status) {
        return ErrorDTO.builder()
                .msg(object)
                .time(LocalDateTime.now())
                .status(status.value())
                .build();
    }
}
