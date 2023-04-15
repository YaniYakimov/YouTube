package com.youtube.youtube.model.DTOs;

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
    private LocalDateTime dateCreated;
    private String name;
    private int visibilityId;
    private long views;
    private String videoUrl;
}
