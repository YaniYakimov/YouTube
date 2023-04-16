package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.CommentBasicDTO;
import com.youtube.youtube.model.DTOs.UserWithoutPassDTO;
import com.youtube.youtube.model.entities.Comment;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.entities.Video;
import com.youtube.youtube.model.exceptions.BadRequestException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService extends AbstractService{
    public List<CommentBasicDTO> sort() {
        return commentRepository.findAll()
                .stream()
                .sorted((o1, o2) -> o2.getDateCreated().compareTo(o1.getDateCreated()))
                .map(c -> mapper.map(c, CommentBasicDTO.class))
                .collect(Collectors.toList());
    }

    public List<CommentBasicDTO> get(int videoId) {
        return null;//TODO
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

    public void react(int subscriberId, int commentId) {
        //todo
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
}
