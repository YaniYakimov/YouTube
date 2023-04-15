package com.youtube.youtube.model.DTOs;

import java.time.LocalDateTime;

public record SearchVideoDTO (int id, UserBasicInfoDTO owner, LocalDateTime dateCreated,
                              String name, long views, String videoUrl) {
}
