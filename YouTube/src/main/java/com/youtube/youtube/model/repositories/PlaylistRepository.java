package com.youtube.youtube.model.repositories;
import com.youtube.youtube.model.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
}
