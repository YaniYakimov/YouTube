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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService extends AbstractService{
    @Autowired
    CommentReactionRepository commentReactionRepository;
    public List<CommentBasicDTO> sort(int videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new NotFoundException(NO_SUCH_VIDEO));
        return commentRepository.findAllByVideo(video)
                .stream()
                .sorted((o1, o2) -> o2.getDateCreated().compareTo(o1.getDateCreated()))
                .map(c -> mapper.map(c, CommentBasicDTO.class))
                .collect(Collectors.toList());
    }
    public List<CommentReplyDTO> get(int videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new BadRequestException(NO_SUCH_VIDEO));
        return video.getComments()
                .stream()
                .map(c -> mapper.map(c, CommentReplyDTO.class))
                .collect(Collectors.toList());
    }
    public CommentCreateDTO createComment(CommentCreateDTO dto, int userId, int videoId) {
        User user = getUserById(userId);
        Comment comment = mapper.map(dto, Comment.class);
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new NotFoundException(NO_SUCH_VIDEO));
        comment.setVideo(video);
        comment.setOwner(user);
        commentRepository.save(comment);
        return mapper.map(comment, CommentCreateDTO.class);
    }
    public CommentBasicDTO react(int userId, int commentId, int reaction) {
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
        CommentBasicDTO updatedComment = mapper.map(comment, CommentBasicDTO.class);
        updatedComment.setLikes(likes);
        updatedComment.setDislikes(dislikes);
        return updatedComment;
    }
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
        commentRepository.save(newComment);
        return mapper.map(newComment, CommentReplyDTO.class);
    }
}