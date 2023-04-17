package com.youtube.youtube.model.DTOs;

import com.youtube.youtube.model.entities.Comment;
import com.youtube.youtube.model.entities.Video;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class CommentBasicDTO {
    private UserWithoutPassDTO owner;
    private Video video;
    private String content;
    private LocalDateTime dateCreated;
    private int isFixed;
    private Comment parent;
    private int likes;
    private int dislikes;
}
