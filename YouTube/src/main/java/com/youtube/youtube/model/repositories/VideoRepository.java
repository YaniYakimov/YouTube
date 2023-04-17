package com.youtube.youtube.model.repositories;
import com.youtube.youtube.model.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    Optional<Video> findByName(String name);
    List<Video> findAllByName(String name);
    @Query(value = "SELECT * FROM videos WHERE name LIKE %:name%", nativeQuery = true)
    List<Video> findAllByTitle(String name);
}
