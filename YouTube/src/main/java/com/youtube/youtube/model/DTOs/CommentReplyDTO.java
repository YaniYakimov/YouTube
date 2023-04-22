package com.youtube.youtube.model.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class CommentReplyDTO {
    private int id;
    private UserWithoutPassDTO owner;
    private VideoInfoDTO video;
    private String content;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
    private int isFixed;
    private int likes;
    private int dislikes;
    private CommentReplyDTO parent;
}