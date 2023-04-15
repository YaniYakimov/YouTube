package com.youtube.youtube.model.DTOs;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record RegisterDTO(@Email(message = "Invalid email") String email,
                          @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+,.?\":{}|<>]){8,}$\n", message = "Week password") String password,
                          @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+,.?\":{}|<>]){8,}$\n", message = "Week password")String confirmPassword,
                          String firstName, String lastName, LocalDateTime dateOfBirth, LocalDateTime dateCreated, char gender, int location) {
}
