package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.CommentBasicDTO;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import com.youtube.youtube.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController extends AbstractController{
    @Autowired
    private CommentService commentService;
    @GetMapping("/comments/sort")
    public List<CommentBasicDTO> getById(@PathVariable int id) {
        return commentService.sort();
    }
    @GetMapping("/comments/{video-id}")
    public List<CommentBasicDTO> getByVideoId(@PathVariable("video-id") int videoId) {
        return commentService.get(videoId);
    }
    @PostMapping("/comments/{video-id}")
    public CommentBasicDTO createComment(@RequestBody CommentBasicDTO dto, HttpSession session, @PathVariable("video-id") int videoId) {
        int userId;
        if(session.getAttribute(LOGGED_ID) == null) {
            throw new UnauthorizedException("You have to logIn first!");
        }
        else {
            userId = (int)session.getAttribute(LOGGED_ID);
        }
        return commentService.createComment(dto, userId, videoId);
    }
    @PostMapping("/comments/{id}")
    public void react(@PathVariable int commentId, HttpSession session) {
        int subscriberId = getLoggedId(session);
        commentService.react(subscriberId, commentId);
    }
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable int id, HttpSession s) {
        int loggedId = getLoggedId(s);
        commentService.deleteComment(id, loggedId);
        return ResponseEntity.ok("Comment deleted successfully.");
    }
}
