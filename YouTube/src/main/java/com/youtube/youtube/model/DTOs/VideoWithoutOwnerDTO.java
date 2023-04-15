package com.youtube.youtube.model.DTOs;

import java.time.LocalDateTime;

public record VideoWithoutOwnerDTO (int id, LocalDateTime dateCreated, String name, int visibility, long views, String videoUrl) {
}
