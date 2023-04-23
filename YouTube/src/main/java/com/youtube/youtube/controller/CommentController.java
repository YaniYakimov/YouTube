package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class CommentController extends AbstractController{
    @Autowired
    private CommentService commentService;
    @GetMapping("/comments/{video-id}/sort")
    public Page<CommentReplyDTO> sortByVideoId(@PathVariable("video-id") int id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated"));
        return commentService.sort(id, pageable);
    }
    @GetMapping("/comments/{video-id}")
    public Page<CommentReplyDTO> getByVideoId(@PathVariable("video-id") int videoId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "dateCreated"));
        return commentService.get(videoId, pageable);
    }
    @PostMapping("/comments/{video-id}/create")
    public CommentCreateDTO createComment(@RequestBody CommentCreateDTO dto, @RequestHeader("Authorization") String authHeader, @PathVariable("video-id") int videoId) {
        int loggedId = getUserId(authHeader);
        return commentService.createComment(dto, loggedId, videoId);
    }
    @PostMapping("/comments/{id}/reaction")
    public CommentReplyDTO react(@PathVariable("id") int commentId, @RequestHeader("Authorization") String authHeader, @RequestBody Integer reaction) {
        int loggedId = getUserId(authHeader);
        return commentService.react(loggedId, commentId, reaction);
    }
    @PostMapping("/comments/{id}/reply")
    public CommentReplyDTO replyToComment (@RequestBody CommentReplyDTO dto, @RequestHeader("Authorization") String authHeader, @PathVariable("id") int parentCommentId) {
        int loggedId = getUserId(authHeader);
        return commentService.reply(dto, loggedId, parentCommentId);
    }
    @PutMapping("/comments/{id}/video/{video-id}")
    public CommentBasicDTO editComment(@PathVariable ("id") int commentId, @RequestBody CommentBasicDTO editDTO, @RequestHeader("Authorization") String authHeader, @PathVariable ("video-id") int videoId){
        int loggedId = getUserId(authHeader);
        return commentService.editComment(loggedId, commentId, editDTO, videoId);
    }
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable int id, @RequestHeader("Authorization") String authHeader) {
        getUserId(authHeader);
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully.");
    }
}
