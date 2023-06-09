package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.CommentBasicDTO;
import com.youtube.youtube.model.DTOs.CommentCreateDTO;
import com.youtube.youtube.model.DTOs.CommentReplyDTO;
import com.youtube.youtube.model.entities.*;
import com.youtube.youtube.model.exceptions.BadRequestException;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import com.youtube.youtube.model.repositories.CommentReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService extends AbstractService{
    @Autowired
    CommentReactionRepository commentReactionRepository;
    public Page<CommentReplyDTO> sort(int videoId, Pageable pageable) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new NotFoundException(NO_SUCH_VIDEO));
        Page<Comment> comments = commentRepository.findAllByVideo(video, pageable);
        return comments.map(c -> mapper.map(c, CommentReplyDTO.class));
    }
    public Page<CommentReplyDTO> get(int videoId, Pageable pageable) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new NotFoundException(NO_SUCH_VIDEO));
        Page<Comment> comments = commentRepository.findAllByVideo(video, pageable);
        return comments.map(c -> mapper.map(c, CommentReplyDTO.class));
    }
    public CommentCreateDTO createComment(CommentCreateDTO dto, int userId, int videoId) {
        User user = getUserById(userId);
        Comment comment = mapper.map(dto, Comment.class);
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new NotFoundException(NO_SUCH_VIDEO));
        comment.setVideo(video);
        comment.setOwner(user);
        comment.setDateCreated(LocalDateTime.now());
        commentRepository.save(comment);
        return mapper.map(comment, CommentCreateDTO.class);
    }
    public CommentReplyDTO react(int userId, int commentId, int reaction) {
        validReaction(reaction);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(NO_SUCH_COMMENT));
        Optional<CommentReaction> existingReaction = commentReactionRepository.findById(userId, commentId);
        if(existingReaction.isPresent() && existingReaction.get().getReaction() == reaction) {
            commentReactionRepository.delete(userId, commentId);
        }
        else {
            commentReactionRepository.save(userId, commentId, reaction);
        }
        int likes = commentReactionRepository.countByCommentIdAndReaction(commentId, LIKE);
        int dislikes = commentReactionRepository.countByCommentIdAndReaction(commentId, DISLIKE);
        CommentReplyDTO updatedComment = mapper.map(comment, CommentReplyDTO.class);
        updatedComment.setLikes(likes);
        updatedComment.setDislikes(dislikes);
        return updatedComment;
    }
    @Transactional
    public void deleteComment(int id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new BadRequestException(NO_SUCH_COMMENT));
        commentRepository.delete(comment);
    }
    public CommentBasicDTO editComment(int userId, int commentId, CommentBasicDTO editDTO, int videoId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(NO_SUCH_COMMENT));
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new NotFoundException(NO_SUCH_VIDEO));
        comment.setVideo(video);
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("You have no permission to edit"));
        comment.setOwner(user);
        comment.setContent(editDTO.getContent());
        comment.setParent(editDTO.getParent());
        comment.setDateCreated(LocalDateTime.now());
        comment.setIsFixed(1);
        commentRepository.save(comment);
        return mapper.map(comment, CommentBasicDTO.class);
    }
    public CommentReplyDTO reply(CommentReplyDTO dto, int userId, int parentCommentId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(NO_SUCH_USER));
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() -> new NotFoundException(NO_SUCH_COMMENT));
        Comment newComment = mapper.map(dto, Comment.class);
        newComment.setParent(parentComment);
        newComment.setOwner(owner);
        newComment.setVideo(parentComment.getVideo());
        newComment.setDateCreated(LocalDateTime.now());
        commentRepository.save(newComment);
        return mapper.map(newComment, CommentReplyDTO.class);
    }
}