package com.youtube.youtube.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VideoReaction {
    @EmbeddedId
    UserReactToVideo id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @MapsId("videoId")
    @JoinColumn(name = "video_id")
    private Video video;
    @Column
    int reaction;
}
