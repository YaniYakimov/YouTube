package com.youtube.youtube.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column
    private String email;
    @Column
    private String password;
    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @Column
    private char gender;

    private int location;
    @Column
    private String telephone;
    @Column(name = "profile_picture_url")
    private String profileImageUrl;
    @OneToMany(mappedBy = "user")
    private Set<Video> videos = new HashSet<>();
    @OneToMany(mappedBy = "user")
    private Set<Playlist> playlists = new HashSet<>();
    @OneToMany(mappedBy = "user")
    Set <VideoHistory> videoHistories = new HashSet<>();
}
