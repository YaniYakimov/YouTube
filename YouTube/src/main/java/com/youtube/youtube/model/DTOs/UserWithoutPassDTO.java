package com.youtube.youtube.model.DTOs;

import jakarta.persistence.Column;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public record UserWithoutPassDTO (int id, String firstName, String lastName, String email, LocalDateTime dateOfBirth, LocalDateTime dateCreated,
                                  char gender, int location, String telephone, String profileImageUrl) {
}
