package com.youtube.youtube.controller;

import com.youtube.youtube.model.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpSession;

public abstract class AbstractController {
    protected static final String LOGGED = "LOGGED";
    protected static final String LOGGED_ID = "LOGGED_ID";

    protected int getLoggedId (HttpSession s){
        if(s.getAttribute(LOGGED) == null){
            throw new UnauthorizedException("You have to login first.");
        }
        return (int) s.getAttribute(LOGGED_ID);
    }
}
