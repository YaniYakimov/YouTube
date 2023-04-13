package com.youtube.youtube.model.entities;

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


}
