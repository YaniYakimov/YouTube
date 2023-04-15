package com.youtube.youtube.model.DTOs;

import java.time.LocalDateTime;
import java.util.Set;

public record PlaylistInfoDTO (int id, UserBasicInfoDTO owner, LocalDateTime dateCreated, String name, String description,
                              int views, String playlistUrl, int visibility, Set<VideoWithoutOwnerDTO> videos) {
}
