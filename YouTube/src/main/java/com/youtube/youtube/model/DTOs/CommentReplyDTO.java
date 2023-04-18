package com.youtube.youtube.model.DTOs;

import com.youtube.youtube.model.entities.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class CommentReplyDTO {
    private UserWithoutPassDTO owner;
    private VideoInfoDTO video;
    private String content;
    private LocalDateTime dateCreated;
    private int isFixed;
    private int likes;
    private int dislikes;
}
