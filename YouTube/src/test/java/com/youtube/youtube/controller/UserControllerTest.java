package com.youtube.youtube.controller;

import com.youtube.youtube.JwtUtil;
import com.youtube.youtube.model.DTOs.LoginDTO;
import com.youtube.youtube.model.DTOs.LoginResponse;
import com.youtube.youtube.model.DTOs.UserWithoutPassDTO;
import com.youtube.youtube.service.UserService;
import io.jsonwebtoken.lang.Assert;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;

    @Mock
    private JwtUtil jwtUtils;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        userController = new UserController();
        userController.setJwtUtils(jwtUtils);
        userController.setUserService(userService);
    }

    @Test
    void login() {
        // Setup mock input data
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPassword("testpassword");

        // Setup mock output data
        UserWithoutPassDTO userDTO = new UserWithoutPassDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("testuser");

        Mockito.when(userService.login(loginDTO)).thenReturn(userDTO);
        Mockito.when(jwtUtils.generateToken(userDTO.getId())).thenReturn("mockToken");
        Mockito.when(jwtUtils.generateRefreshToken(userDTO.getId())).thenReturn("mockRefreshToken");

        // Call login method
        ResponseEntity<LoginResponse> response = userController.login(loginDTO);

        // Verify response
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        HttpHeaders headers = response.getHeaders();
        Assert.assertNotNull(headers.get("Authorization"));
        Assert.assertEquals("Bearer mockToken", headers.getFirst("Authorization"));

        LoginResponse responseBody = response.getBody();
        Assert.assertEquals(userDTO, responseBody.getUser());
        Assert.assertEquals("mockToken", responseBody.getToken());
        Assert.assertEquals("mockRefreshToken", responseBody.getRefreshToken());
    }
    }
}