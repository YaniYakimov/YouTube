package com.youtube.youtube.model.DTOs;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public class UserWithoutPassDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime dateOfBirth;
    private LocalDateTime dateCreated;
    private char gender;
    private int location;
    private String telephone;
    private String profileImageUrl;
}
