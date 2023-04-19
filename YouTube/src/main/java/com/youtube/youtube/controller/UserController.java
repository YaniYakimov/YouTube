package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import com.youtube.youtube.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    @GetMapping("/users/confirm")
    public ResponseEntity<String> validateRegistration(@RequestParam("token")String token) {
        if(userService.confirmEmail(token)){
            return ResponseEntity.ok("Validation was successful");
        }else {
            return ResponseEntity.ok("Validation was unsuccessful");
        }
    }
    @PostMapping("/users")
    public UserWithoutPassDTO register(@Valid @RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }
    @PostMapping("/users/sign-in")
    public ResponseEntity<UserWithoutPassDTO> login(@RequestBody LoginDTO dto) {
        UserWithoutPassDTO respDTO = userService.login(dto);
        String token = jwtUtils.generateToken(respDTO.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return ResponseEntity.ok().headers(headers).body(respDTO);
    }
    @PostMapping("/users/sign-out")
    public ResponseEntity<String> logOut(@RequestHeader("Authorization") String authHeader) {
        int loggedId = getUserId(authHeader);
        if(loggedId != 0) {
            String body = "Log-out was successful.";
            addToBlacklist(authHeader, body);
        }
        throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
    }
    @PostMapping("/users/search")
    public List<UserWithoutPassDTO> searchByName(@RequestBody UserBasicInfoDTO dto) {
        return userService.getUserByName(dto);
    }
    @PostMapping("/users/{id}/subscribe")
    public int subscribe(@PathVariable int id, @RequestHeader("Authorization") String authHeader) {
        int subscriberId = getUserId(authHeader);
        if(subscriberId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        return userService.subscribe(subscriberId, id);
    }
    @PutMapping("/users")
    public UserWithoutPassDTO edit(@Valid @RequestBody RegisterDTO dto, @RequestHeader("Authorization") String authHeader) {
        int loggedId = getUserId(authHeader);
        if(loggedId != 0) {
            return userService.edit(dto, loggedId);
        }
        throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
    }
    @DeleteMapping("/users")
    public ResponseEntity<String> deleteAccount(@RequestHeader("Authorization") String authHeader) {
        int loggedId = getUserId(authHeader);
        if(loggedId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        String body = "Delete was successful.";
        addToBlacklist(authHeader, body);
        userService.deleteAccount(loggedId);
        return ResponseEntity.ok(body);
    }
}
