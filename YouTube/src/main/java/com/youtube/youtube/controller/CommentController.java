package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
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
    @GetMapping("/comments/{video-id}/sort")
    public List<CommentReplyDTO> getById(@PathVariable("video-id") int id) {
        return commentService.sort(id);
    }
    @GetMapping("/comments/{video-id}")
    public List<CommentReplyDTO> getByVideoId(@PathVariable("video-id") int videoId) {
        return commentService.get(videoId);
    }
    @PostMapping("/comments/{video-id}/create")
    public CommentCreateDTO createComment(@RequestBody CommentCreateDTO dto, HttpSession session, @PathVariable("video-id") int videoId) {
        int userId;
        if(session.getAttribute(LOGGED_ID) == null) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        else {
            userId = (int)session.getAttribute(LOGGED_ID);
        }
        return commentService.createComment(dto, userId, videoId);
    }
    @PostMapping("/comments/{id}/reaction")
    public CommentReplyDTO react(@PathVariable("id") int commentId, HttpSession session, @RequestBody Integer reaction) {
        int userId = getLoggedId(session);
        return commentService.react(userId, commentId, reaction);
    }
    @PostMapping("/comments/{id}/reply")
    public CommentReplyDTO replyToComment (@RequestBody CommentReplyDTO dto, HttpSession session, @PathVariable("id") int parentCommentId) {
        int userId;
        if(session.getAttribute(LOGGED_ID) == null) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        else {
            userId = (int)session.getAttribute(LOGGED_ID);
        }
        return commentService.reply(dto, userId, parentCommentId);
    }
    @PutMapping("/comments/{id}/video/{video-id}")
    public CommentBasicDTO editComment(@PathVariable ("id") int commentId, @RequestBody CommentBasicDTO editDTO, HttpSession s, @PathVariable ("video-id") int videoId){
        int userId=getLoggedId(s);
        return commentService.editComment(userId, commentId, editDTO, videoId);
    }
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable int id, HttpSession s) {
        if(s.getAttribute(LOGGED_ID) == null) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        else {
            commentService.deleteComment(id);
            return ResponseEntity.ok("Comment deleted successfully.");
        }
    }
}
