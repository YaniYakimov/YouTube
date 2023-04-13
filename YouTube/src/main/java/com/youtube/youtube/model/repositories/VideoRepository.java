package com.youtube.youtube.model.repositories;
import com.youtube.youtube.model.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Integer> {
}
