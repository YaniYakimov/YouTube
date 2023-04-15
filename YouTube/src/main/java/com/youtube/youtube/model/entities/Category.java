package com.youtube.youtube.model.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String type;
//    @OneToMany(mappedBy = "category")
//    private Set<Video> videos = new HashSet<>();

}
