package com.youtube.youtube.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor

public class VideoInfoDTO {
        private int id;
        private UserBasicInfoDTO user;
        private LocalDateTime dateCreated;
        private String name;
        private String description;
        private long views;
        private String videoUrl;
        private VisibilityDTO visibility;
        private CategoryDTO category;
}
