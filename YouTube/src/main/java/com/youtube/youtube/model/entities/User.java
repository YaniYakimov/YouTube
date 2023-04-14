package com.youtube.youtube.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
=======
import java.util.List;
>>>>>>> f826aecb783836ffffbbfc5339c33590fc038668

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
    @Column
    private int location;
    @Column
    private String telephone;
    @Column(name = "profile_picture_url")
    private String profileImageUrl;
<<<<<<< HEAD
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_have_subscribers",
            joinColumns = @JoinColumn(name = "subscribed_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id")
    )
    private Set<User> subscribers = new HashSet<>();
    @ManyToMany(mappedBy = "subscribers")
    private Set<User> subscribedTo = new HashSet<>();
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentReaction> commentReactions = new HashSet<>();
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoReaction> videoReactions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
=======
    @OneToMany(mappedBy = "user")
    private List<Video> videos;
    @OneToMany(mappedBy = "user")
    private List<Playlist> playlists;
>>>>>>> f826aecb783836ffffbbfc5339c33590fc038668
}
