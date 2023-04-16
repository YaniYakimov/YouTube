package com.youtube.youtube.model.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//public record VideoInfoDTO (int id, UserBasicInfoDTO owner, LocalDateTime dateCreated, String name, String description,
//                            long views, String videoUrl, int visibility, int category) {
//}
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
//        private int visibilityId;
//        private int categoryId;
        private VisibilityDTO visibility;
        private CategoryDTO category;
}
