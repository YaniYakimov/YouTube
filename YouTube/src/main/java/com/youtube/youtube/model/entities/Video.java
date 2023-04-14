package com.youtube.youtube.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

public class Video {
    @OneToMany(mappedBy = "videos", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoReaction> reactions = new HashSet<>();
}
