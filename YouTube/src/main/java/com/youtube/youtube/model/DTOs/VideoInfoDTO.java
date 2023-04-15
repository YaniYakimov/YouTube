package com.youtube.youtube.model.DTOs;

import java.time.LocalDateTime;

public record VideoInfoDTO (int id, UserBasicInfoDTO owner, LocalDateTime dateCreated, String name, String description,
                            long views, String videoUrl, int visibility, int category) {
}
