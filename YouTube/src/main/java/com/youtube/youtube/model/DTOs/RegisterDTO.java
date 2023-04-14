package com.youtube.youtube.model.DTOs;

import jakarta.persistence.Column;

public record RegisterDTO(String email, String password, String confirmPassword) {
}
