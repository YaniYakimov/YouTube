package com.youtube.youtube.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visibility_id",nullable = false)
    private Visibility visibility;
//    @Column(name = "visibility_id")
//    private int visibilityId;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoReaction> reactions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
//    @Column(name = "category_id")
//    private int categoryId;

    @ManyToMany(mappedBy = "videos")
    private Set<Playlist> playlists = new HashSet<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    Set <VideoHistory> videoHistories = new HashSet<>();
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return id == video.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
