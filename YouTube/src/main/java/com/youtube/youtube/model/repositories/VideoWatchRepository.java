package com.youtube.youtube.model.repositories;

import com.youtube.youtube.model.entities.UserReactToVideo;
import com.youtube.youtube.model.entities.UserWatchVideo;
import com.youtube.youtube.model.entities.VideoHistory;
import com.youtube.youtube.model.entities.VideoReaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VideoWatchRepository extends JpaRepository<VideoHistory, UserWatchVideo> {
//    @Query(value = "SELECT COUNT(*) FROM users_watch_videos WHERE reaction = :reaction AND video_id = :videoId", nativeQuery = true)
//    int countByVideoIdAndWatches(int videoId, int reaction);

    @Query(value = "SELECT * FROM users_watch_videos WHERE user_id = :userId AND video_id = :videoId", nativeQuery = true)
    Optional<VideoHistory> findById(@Param("userId") int userId, @Param("videoId") int videoId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_watch_videos (user_id, video_id, date_watched) VALUES (:userId, :videoId, NOW()) ON DUPLICATE KEY UPDATE date_watched = NOW()", nativeQuery = true)
    void save(@Param("userId") int userId, @Param("videoId") int videoId);

//    @Modifying
//    @Query(value = "DELETE FROM users_react_to_videos WHERE user_id = :userId AND video_id = :videoId", nativeQuery = true)
//    void delete(@Param("userId") int userId, @Param("videoId") int videoId);

}
