package com.youtube.youtube.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
    private LocalDate dateOfBirth;
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @Column
    private String gender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
    @Column
    private String telephone;
    @Column(name = "profile_picture_url")
    private String profilePicture;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_have_subscribers",
            joinColumns = @JoinColumn(name = "subscribed_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id")
    )
    private Set<User> subscribers = new HashSet<>();
    @ManyToMany(mappedBy = "subscribers")
    private Set<User> subscribedTo = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentReaction> commentReactions = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoReaction> videoReactions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Video> videos = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Playlist> playlists = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set <VideoHistory> videoHistories = new HashSet<>();
    @OneToMany(mappedBy = "owner")
    private List<Comment> comments = new ArrayList<>();


//    private List<Playlist> playlists;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }
}
