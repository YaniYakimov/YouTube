package com.youtube.youtube.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class VideoReactionDTO {
    private int id;
    private UserBasicInfoDTO user;
    private String name;
    private long views;
    private int likes;
    private int dislikes;
}
