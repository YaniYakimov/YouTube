package com.youtube.youtube.model.DTOs;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record ErrorDTO(Object msg, int status, LocalDateTime time) {
}
