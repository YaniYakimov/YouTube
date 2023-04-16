package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.entities.User;
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

    @GetMapping("/users/{id}")
    public UserWithoutPassDTO getById(@PathVariable int id) {
        return userService.getById(id);
    }
    @PostMapping("/users")
    public UserWithoutPassDTOTest register(@Valid @RequestBody RegisterDTOTest dto) {
        return userService.register(dto);
    }
    @PostMapping("/users/sign-in")
    public UserWithoutPassDTO login(@RequestBody LoginDTO dto, HttpSession s) {
        UserWithoutPassDTO respDTO = userService.login(dto);
        s.setAttribute(LOGGED, true);
        s.setAttribute(LOGGED_ID, respDTO.id());
        return respDTO;
    }
    @PostMapping("/users/sign-out")
    public ResponseEntity<String> logOut(HttpSession s) {
        userService.logOut(s);
        return ResponseEntity.ok("Log-out was successful.");
    }
    @PostMapping("/users/search")
    public List<User> searchByName(@RequestBody UserBasicInfoDTO dto) {
        return userService.getUserByName(dto);
    }
    @PostMapping("/users/{subscribedId}/subscribe")
    public int subscribe(@PathVariable int subscribedId, HttpSession session) {
        int subscriberId = getLoggedId(session);
        return userService.subscribe(subscriberId, subscribedId);
    }
    @PutMapping("localhost:8995/users")
    public UserWithoutPassDTOTest edit(@Valid @RequestBody RegisterDTOTest dto, HttpSession session) {
        int loggedId = getLoggedId(session);
        return userService.edit(dto);
    }
    @DeleteMapping("/users")
    public ResponseEntity<String> deleteAccount(HttpSession s) {
        int loggedId = getLoggedId(s);
        userService.deleteAccount(loggedId);
        s.invalidate();
        return ResponseEntity.ok("Account deleted successfully.");
    }
}
