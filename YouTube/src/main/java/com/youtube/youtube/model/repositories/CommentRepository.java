package com.youtube.youtube.model.repositories;
import com.youtube.youtube.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
