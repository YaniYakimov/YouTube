package com.youtube.youtube.model.entities;

<<<<<<< HEAD
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

public class Video {
    @OneToMany(mappedBy = "videos", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoReaction> reactions = new HashSet<>();
=======
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @Column(name= "date_created")
    private LocalDateTime dateCreated;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private long views;
    @Column(name = "video_url")
    private String videoUrl;
    //todo visibility_id category_id
    private int visibilityId;
    private int categoryId;


>>>>>>> f826aecb783836ffffbbfc5339c33590fc038668
}
