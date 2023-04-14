package com.youtube.youtube.model.DTOs;

import com.youtube.youtube.model.entities.Video;

import java.time.LocalDateTime;

public record CommentBasicDTO(UserWithoutPassDTO owner, Video video, String content, LocalDateTime dateCreated, int isFixed) {
}
