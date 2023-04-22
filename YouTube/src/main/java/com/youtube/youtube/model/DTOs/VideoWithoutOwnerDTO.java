package com.youtube.youtube.model.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//public record VideoWithoutOwnerDTO (int id, LocalDateTime dateCreated, String name, int visibility, long views, String videoUrl) {
//}
@Getter
@Setter
@NoArgsConstructor

public class VideoWithoutOwnerDTO{
    private int id;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
    private String name;
    private VisibilityDTO visibility;
    private long views;
    private String videoUrl;
}
