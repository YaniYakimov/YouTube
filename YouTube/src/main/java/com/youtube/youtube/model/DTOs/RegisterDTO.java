package com.youtube.youtube.model.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class RegisterDTO {
    private int id;
    @Email(message = "Invalid email")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+,.?\":{}|<>])(?=\\S+$).{8,}$", message = "Week password")
    private String password;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+,.?\":{}|<>])(?=\\S+$).{8,}$", message = "Week password")
    private String confirmPassword;
    @Pattern(regexp = "^[A-Z][a-z]{1,}$", message = "Name should start with upper letter and to be at least 2 symbols")
    private String firstName;
    @Pattern(regexp = "^[A-Z][a-z]{1,}$", message = "Name should start with upper letter and to be at least 2 symbols")
    private String lastName;
    private LocalDate dateOfBirth;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
    @Pattern(regexp = "^(M|F)$", message = "Wrong gender")
    private String gender;
    private String location;
    private String telephone;
    private String profilePicture;
}