package com.youtube.youtube.model.DTOs;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record RegisterDTO(@Email(message = "Invalid email") String email,
                          @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+,.?\":{}|<>])[A-Za-z\\d!@#$%^&*()_+,.?\":{}|<>]{8,}$\n", message = "Week password") String password,
                          @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+,.?\":{}|<>])[A-Za-z\\d!@#$%^&*()_+,.?\":{}|<>]{8,}$\n", message = "Week password")String confirmPassword) {
}
