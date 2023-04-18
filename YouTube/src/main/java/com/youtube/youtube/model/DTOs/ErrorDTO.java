package com.youtube.youtube.model.DTOs;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDTO {
    private Object msg;
    private int status;
    private LocalDateTime time;


}
