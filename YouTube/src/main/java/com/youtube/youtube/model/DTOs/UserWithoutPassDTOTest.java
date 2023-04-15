package com.youtube.youtube.model.DTOs;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserWithoutPassDTOTest {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime dateOfBirth;
    private LocalDateTime dateCreated;
    private char gender;
    private int location;

}
