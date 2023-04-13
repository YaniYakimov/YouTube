package com.youtube.youtube.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "videos")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private int views;
    @Column(name = "playlist_url")
    private String playlistUrl;

    // todo visibility_id

    //todo many to many Video;
}
