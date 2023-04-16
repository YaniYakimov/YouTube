package com.youtube.youtube.model.repositories;

import com.youtube.youtube.model.entities.UserReactToVideo;
import com.youtube.youtube.model.entities.VideoReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoReactionRepository extends JpaRepository<VideoReaction, UserReactToVideo> {
}
