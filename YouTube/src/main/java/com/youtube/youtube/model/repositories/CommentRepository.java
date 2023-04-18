package com.youtube.youtube.model.repositories;
import com.youtube.youtube.model.entities.Comment;
import com.youtube.youtube.model.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findAllByVideo(Video video);
}
