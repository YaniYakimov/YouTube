package com.youtube.youtube.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VideoHistory {
    @EmbeddedId
    private UserWatchVideo id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne
    @MapsId("videoId")
    @JoinColumn(name="video_id")
    private Video video;
    @Column(name = "date_watched")
    private LocalDateTime dateWatched;
}
