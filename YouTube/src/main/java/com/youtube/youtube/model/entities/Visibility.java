package com.youtube.youtube.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Setter
@Getter
@Entity(name = "visibilities")
public class Visibility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String type;
    @OneToMany(mappedBy = "visibility")
    private Set<Video> videos = new HashSet<>();
    @OneToMany(mappedBy = "visibility")
    private Set<Playlist> playlists = new HashSet<>();
}
