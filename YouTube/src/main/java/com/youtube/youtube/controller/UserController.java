package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import com.youtube.youtube.service.UserService;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Setter
@RestController
public class UserController extends AbstractController{
    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public UserWithoutPassDTO getById(@PathVariable int id) {
        return userService.getById(id);
    }
    @GetMapping("/users/confirm")
    public ResponseEntity<String> validateRegistration(@RequestParam("token")String token) {
        userService.confirmEmail(token);
        return ResponseEntity.ok("Validation was successful");
    }
    @PostMapping("/users")
    public UserWithoutPassDTO register(@Valid @RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }
    @PostMapping("/users/sign-in")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO dto) {
        UserWithoutPassDTO respDTO = userService.login(dto);
        String token = jwtUtils.generateToken(respDTO.getId());
        String refreshToken = jwtUtils.generateRefreshToken(respDTO.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        LoginResponse loginResponse = new LoginResponse(respDTO, token, refreshToken);
        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }
    @PostMapping("/users/sign-out")
    public ResponseEntity<String> logOut(@RequestHeader("Authorization") String authHeader) {
        getUserId(authHeader);
        String body = "Log-out was successful.";
        addToBlacklist(authHeader, body);
        return ResponseEntity.ok(body);
    }
    @PostMapping("/users/search")
    public Page<UserWithoutPassDTO> searchByName(@RequestBody UserBasicInfoDTO dto, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return userService.getUserByName(dto, page, size);
    }
    @PostMapping("/users/{id}/subscribe")
    public int subscribe(@PathVariable int id, @RequestHeader("Authorization") String authHeader) {
        int subscriberId = getUserId(authHeader);
        return userService.subscribe(subscriberId, id);
    }
    @PutMapping("/users")
    public UserWithoutPassDTO edit(@Valid @RequestBody RegisterDTO dto, @RequestHeader("Authorization") String authHeader) {
        int loggedId = getUserId(authHeader);
        return userService.edit(dto, loggedId);
    }
    @DeleteMapping("/users")
    public ResponseEntity<String> deleteAccount(@RequestHeader("Authorization") String authHeader) {
        int loggedId = getUserId(authHeader);
        String body = "Delete was successful.";
        addToBlacklist(authHeader, body);
        userService.deleteAccount(loggedId);
        return ResponseEntity.ok(body);
    }
}
