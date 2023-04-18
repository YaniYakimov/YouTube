package com.youtube.youtube.model.repositories;

import com.youtube.youtube.model.entities.CommentReaction;
import com.youtube.youtube.model.entities.UserReactToComment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CommentReactionRepository  extends JpaRepository<CommentReaction, UserReactToComment> {
    @Query(value = "SELECT * FROM users_react_to_comments WHERE user_id = :userId AND comment_id = :commentId", nativeQuery = true)
    Optional<CommentReaction> findById(@Param("userId") int userId, @Param("commentId") int commentId);
    @Modifying
    @Query(value = "DELETE FROM users_react_to_comments WHERE user_id = :userId AND comment_id = :commentId", nativeQuery = true)
    void delete(@Param("userId") int userId, @Param("commentId") int videoId);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_react_to_comments (user_id, comment_id, reaction) VALUES (:userId, :commentId, :reaction) ON DUPLICATE KEY UPDATE reaction = :reaction", nativeQuery = true)
    void save(@Param("userId") int userId, @Param("commentId") int commentId, @Param("reaction") int reaction);
    @Query(value = "SELECT COUNT(*) FROM users_react_to_comments WHERE reaction = :reaction AND comment_id = :commentId", nativeQuery = true)
    int countByCommentIdAndReaction(int commentId, int reaction);
}
