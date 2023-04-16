package com.youtube.youtube.model.DTOs;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ErrorDTO {
    private Object msg;
    private int status;
    private LocalDateTime time;
}
