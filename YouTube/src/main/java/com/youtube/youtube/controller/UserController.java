package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import com.youtube.youtube.service.EmailSenderService;
import com.youtube.youtube.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController extends AbstractController{
    @Autowired
    private UserService userService;
    @Autowired
    private EmailSenderService mailSender;

    @GetMapping("/users/{id}")
    public UserWithoutPassDTO getById(@PathVariable int id) {
        return userService.getById(id);
    }
    @PostMapping("/users")
    public UserWithoutPassDTO register(@Valid @RequestBody RegisterDTO dto) {
        mailSender.sendEmail("yani.v.yakimov@gmail.com", "Welcome", "Cheers, you just got registered");
        return userService.register(dto);
    }
    @PostMapping("/users/sign-in")
    public UserWithoutPassDTO login(@RequestBody LoginDTO dto, HttpSession s) {
        UserWithoutPassDTO respDTO = userService.login(dto);
        s.setAttribute(LOGGED, true);
        s.setAttribute(LOGGED_ID, respDTO.getId());
        return respDTO;
    }
    @PostMapping("/users/sign-out")
    public ResponseEntity<String> logOut(HttpSession s) {
        if(s.getAttribute(LOGGED_ID) == null) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        else {
            return ResponseEntity.ok("Log-out was successful.");
        }
    }
    @PostMapping("/users/search")
    public List<UserWithoutPassDTO> searchByName(@RequestBody UserBasicInfoDTO dto) {
        return userService.getUserByName(dto);
    }
    @PostMapping("/users/{id}/subscribe")
    public int subscribe(@PathVariable int id, HttpSession session) {
        int subscriberId = getLoggedId(session);
        return userService.subscribe(subscriberId, id);
    }
    @PutMapping("/users")
    public UserWithoutPassDTO edit(@Valid @RequestBody RegisterDTO dto, HttpSession session) {
        int loggedId = getLoggedId(session);
        if(session.getAttribute(LOGGED_ID) == null) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        else {
            return userService.edit(dto, loggedId);
        }
    }
    @DeleteMapping("/users")
    public ResponseEntity<String> deleteAccount(HttpSession s) {
        int loggedId = getLoggedId(s);
        if(s.getAttribute(LOGGED_ID) == null) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        else {
            userService.deleteAccount(loggedId);
            s.invalidate();
            return ResponseEntity.ok("Account deleted successfully.");
        }
    }
}
