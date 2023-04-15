package com.youtube.youtube.model.DTOs;

import java.util.Set;

public record UserVideosDTO (int id, String firstName, String lastName, Set<VideoWithoutOwnerDTO> videos) {
}
