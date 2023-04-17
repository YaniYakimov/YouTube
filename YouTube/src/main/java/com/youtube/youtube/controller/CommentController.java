package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.CommentBasicDTO;
import com.youtube.youtube.model.DTOs.EditVideoDTO;
import com.youtube.youtube.model.DTOs.VideoInfoDTO;
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
    public CommentBasicDTO react(@PathVariable int commentId, HttpSession session, @RequestBody Integer reaction) {
        int userId = getLoggedId(session);
        return commentService.react(userId, commentId, reaction);
    }
    @PostMapping("/comments/{id}/reply")
    public CommentBasicDTO replyToComment (@RequestBody CommentBasicDTO dto, HttpSession session, @PathVariable("id") int parentCommentId) {
        int userId;
        if(session.getAttribute(LOGGED_ID) == null) {
            throw new UnauthorizedException("You have to logIn first!");
        }
        else {
            userId = (int)session.getAttribute(LOGGED_ID);
        }
        return commentService.reply(dto, userId, parentCommentId);
    }
    @PutMapping("/comments/{id}")
    public CommentBasicDTO editComment(@PathVariable ("id") int commentId, @RequestBody CommentBasicDTO editDTO, HttpSession s){
        int userId=getLoggedId(s);
        return commentService.editComment(userId, commentId, editDTO);
    }
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable int id, HttpSession s) {
        int loggedId = getLoggedId(s);
        commentService.deleteComment(id, loggedId);
        return ResponseEntity.ok("Comment deleted successfully.");
    }
}
