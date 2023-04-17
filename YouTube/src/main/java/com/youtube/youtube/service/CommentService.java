package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.CommentBasicDTO;
import com.youtube.youtube.model.DTOs.VideoReactionDTO;
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
    public List<CommentBasicDTO> sort() {
        return commentRepository.findAll()
                .stream()
                .sorted((o1, o2) -> o2.getDateCreated().compareTo(o1.getDateCreated()))
                .map(c -> mapper.map(c, CommentBasicDTO.class))
                .collect(Collectors.toList());
    }

    public List<CommentBasicDTO> get(int videoId) {
        Optional<Video> video = videoRepository.findById(videoId);
        if(!video.isPresent()) {
            throw new BadRequestException("No such video!");
        }
        return video.get().getComments()
                .stream()
                .map(c -> mapper.map(c, CommentBasicDTO.class))
                .collect(Collectors.toList());
    }

    public CommentBasicDTO createComment(CommentBasicDTO dto, int userId, int videoId) {
        User user = getUserById(userId);
        Comment comment = mapper.map(dto, Comment.class);
        Optional<Video> video = videoRepository.findById(videoId);
        if(video.isPresent()) {
            comment.setVideo(video.get());
        }
        comment.setOwner(user);
        commentRepository.save(comment);
        return mapper.map(comment, CommentBasicDTO.class);
    }

    public CommentBasicDTO react(int userId, int commentId, int reaction) {
        validReaction(reaction);
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(!comment.isPresent()) {
            throw new NotFoundException("No such video!");
        }
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

    public void deleteComment(int id, int loggedId) {
        Optional<User> user = userRepository.findById(loggedId);
        if(!user.isPresent()) {
            throw new BadRequestException("You need to login first!");
        }
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isPresent()) {
            commentRepository.delete(comment.get());
        }
        else {
            throw new BadRequestException("No such comment!");
        }
    }

    public CommentBasicDTO editComment(int userId, int commentId, CommentBasicDTO editDTO) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(!comment.isPresent()) {
            throw new BadRequestException("No such comment!");
        }
        comment.get().setVideo(editDTO.getVideo());
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) {
            throw new UnauthorizedException("You have no permission to edit");
        }
        comment.get().setOwner(user.get());
        comment.get().setContent(editDTO.getContent());
        comment.get().setParent(editDTO.getParent());
        comment.get().setDateCreated(LocalDateTime.now());
        return mapper.map(comment, CommentBasicDTO.class);
    }

    public CommentBasicDTO reply(CommentBasicDTO dto, int userId, int parentCommentId) {
        Optional<Comment> parentComment = commentRepository.findById(parentCommentId);
        if(!parentComment.isPresent()) {
            throw new NotFoundException("No such comment");
        }
        Comment newComment = mapper.map(dto, Comment.class);
        newComment.setParent(parentComment.get());
        commentRepository.save(newComment);
        return mapper.map(newComment, CommentBasicDTO.class);
    }
}
