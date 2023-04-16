package com.youtube.youtube.model.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor

public class RegisterDTOTest {
    @Email(message = "Invalid email")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+,.?\":{}|<>])(?=\\S+$).{8,}$", message = "Week password")
    private String password;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+,.?\":{}|<>])(?=\\S+$).{8,}$", message = "Week password")
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private LocalDateTime dateCreated;
    private char gender;
    private int location;
}