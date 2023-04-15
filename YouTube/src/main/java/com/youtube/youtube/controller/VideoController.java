package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.SearchVideoDTO;
import com.youtube.youtube.model.DTOs.UserVideosDTO;
import com.youtube.youtube.model.DTOs.VideoInfoDTO;
import com.youtube.youtube.service.UserService;
import com.youtube.youtube.service.VideoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VideoController extends AbstractController{
    @Autowired
    private UserService userService;
    @Autowired
    private VideoService videoService;
    @GetMapping("/users/{id}/videos")
    public UserVideosDTO getUserVideos(@PathVariable int id){

        //TODO videoService.getUserVideos(id);
        return null;
    }
    @GetMapping("/videos/{id}")
    public VideoInfoDTO getVideoById(@PathVariable int id){

        return videoService.getVideoById(id);
    }

    @PostMapping("/videos/search")
    public SearchVideoDTO searchVideo(String name){

        return videoService.searchVideo(name);
    }

    @PutMapping("/videos/{id}")
    public VideoInfoDTO editVideo(@PathVariable ("id") int videoId, VideoInfoDTO editData, HttpSession s){
        int userId=getLoggedId(s);
        return videoService.editVideo(userId, videoId, editData);
    }

    @DeleteMapping("/videos/{id}")
    public void deleteVideo(@PathVariable ("id") int videoId, HttpSession s){
        int userId=getLoggedId(s);
        videoService.deleteVideo(userId, videoId);
    }


}
