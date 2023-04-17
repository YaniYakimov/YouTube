package com.youtube.youtube.model.DTOs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithoutPassDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private LocalDateTime dateCreated;
    private String gender;

}
