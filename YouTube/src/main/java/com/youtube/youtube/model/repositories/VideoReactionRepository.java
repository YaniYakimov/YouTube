package com.youtube.youtube.model.repositories;

import com.youtube.youtube.model.entities.UserReactToVideo;
import com.youtube.youtube.model.entities.VideoReaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoReactionRepository extends JpaRepository<VideoReaction, UserReactToVideo> {
    @Query(value = "SELECT COUNT(*) FROM users_react_to_videos WHERE reaction = :reaction AND video_id = :videoId", nativeQuery = true)
    int countByVideoIdAndReaction(int videoId, int reaction);

    @Query(value = "SELECT * FROM users_react_to_videos WHERE user_id = :userId AND video_id = :videoId", nativeQuery = true)
    Optional<VideoReaction> findById(@Param("userId") int userId, @Param("videoId") int videoId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_react_to_videos (user_id, video_id, reaction) VALUES (:userId, :videoId, :reaction) ON DUPLICATE KEY UPDATE reaction = :reaction", nativeQuery = true)
    void save(@Param("userId") int userId, @Param("videoId") int videoId, @Param("reaction") int reaction);

    @Modifying
    @Query(value = "DELETE FROM users_react_to_videos WHERE user_id = :userId AND video_id = :videoId", nativeQuery = true)
    void delete(@Param("userId") int userId, @Param("videoId") int videoId);

}
