package com.youtube.youtube.model.repositories;
import com.youtube.youtube.model.entities.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    @Query(value = "SELECT * FROM videos WHERE name LIKE %:name%", nativeQuery = true)
    Page<Video> findAllByTitle(String name, Pageable pageable);
    @Query(value = "SELECT * FROM videos WHERE user_id = :userId", nativeQuery = true)
    Page<Video> findAllByUser(int userId, Pageable pageable);
    @Query(value = "SELECT * FROM videos AS v JOIN playlists_have_videos AS pv ON (v.id=pv.video_id) WHERE playlist_id = :playlistId", nativeQuery = true)
    Page<Video> findAllInPlaylist(int playlistId, Pageable pageable);
}
