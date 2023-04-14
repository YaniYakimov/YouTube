package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.LoginDTO;
import com.youtube.youtube.model.DTOs.RegisterDTO;
import com.youtube.youtube.model.DTOs.UserWithoutPassDTO;
import com.youtube.youtube.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends AbstractController{
    @Autowired
    private UserService userService;
    @PostMapping("/users")
    public UserWithoutPassDTO register(@RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }
    @PostMapping("/users/sign-in")
    public UserWithoutPassDTO login(@RequestBody LoginDTO dto, HttpSession s) {
        UserWithoutPassDTO respDTO = userService.login(dto);
        s.setAttribute("LOGGED", true);
        s.setAttribute("LOGGED_ID", respDTO.getId());
        return respDTO;
    }
    @GetMapping("/users/{id}")
    public UserWithoutPassDTO getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }
}
