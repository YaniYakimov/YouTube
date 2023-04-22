package com.youtube.youtube.model.repositories;
import com.youtube.youtube.model.entities.Playlist;
import com.youtube.youtube.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    Optional<Playlist> findByNameAndUser(String name, User user);
    @Query(value = "SELECT COUNT(*) FROM playlists_have_videos WHERE playlist_id = ?", nativeQuery = true)
    int countVideosInPlaylist(int playlistId);

    @Query(value = "SELECT * FROM playlists WHERE name LIKE %:name%", nativeQuery = true)
    Page<Playlist> findAllByTitle(String name, Pageable pageable);
}
