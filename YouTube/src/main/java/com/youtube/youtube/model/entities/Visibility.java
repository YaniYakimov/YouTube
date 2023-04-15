package com.youtube.youtube.model.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Visibility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String type;
//    @OneToMany(mappedBy = "visibility")
//    private Set<Video> videos = new HashSet<>();
    @OneToMany(mappedBy = "visibility")
    private Set<Playlist> playlists = new HashSet<>();
}
