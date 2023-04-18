package com.youtube.youtube.model.DTOs;

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
//    @Pattern(regexp = "^(18[9][8-9]|19[0-9]{2}|20[0-1][0-9]|2023)-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", message = "Invalid date of birth")
    private LocalDate dateOfBirth;
    private LocalDateTime dateCreated;
    @Pattern(regexp = "^(M|F)$", message = "Wrong gender")
    private String gender;
    private String location;
    private String telephone;
    private String profilePicture;
}
