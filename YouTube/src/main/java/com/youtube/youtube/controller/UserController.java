package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.LoginDTO;
import com.youtube.youtube.model.DTOs.RegisterDTO;
import com.youtube.youtube.model.DTOs.UserBasicInfoDTO;
import com.youtube.youtube.model.DTOs.UserWithoutPassDTO;
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
    public UserWithoutPassDTO register(@Valid @RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }
    @PostMapping("/users/sign-in")
    public UserWithoutPassDTO login(@Valid @RequestBody LoginDTO dto, HttpSession s) {
        UserWithoutPassDTO respDTO = userService.login(dto);
        s.setAttribute("LOGGED", true);
        s.setAttribute("LOGGED_ID", respDTO.id());
        return respDTO;
    }
    @PostMapping("/users/sign-out")
    public ResponseEntity<String> logOut(HttpSession s) {
        int loggedId = getLoggedId(s);
        userService.logOut(loggedId, s);
        return ResponseEntity.ok("Account deleted successfully.");
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
    @DeleteMapping("/users")
    public ResponseEntity<String> deleteAccount(HttpSession s) {
        int loggedId = getLoggedId(s);
        userService.deleteAccount(loggedId);
        return ResponseEntity.ok("Account deleted successfully.");
    }
}
