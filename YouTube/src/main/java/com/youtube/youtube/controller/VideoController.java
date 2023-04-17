package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.service.VideoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@RestController
public class VideoController extends AbstractController{

    @Autowired
    private VideoService videoService;
    @GetMapping("/users/{id}/videos")
    public UserVideosDTO getUserVideos(@PathVariable int id){
        return videoService.getUserVideos(id);

    }
    @GetMapping("/videos/{id}")
    public VideoInfoDTO getVideoById(@PathVariable int id){
        return videoService.getVideoById(id);
    }

    @PostMapping("/videos")
    public VideoInfoDTO uploadVideo(@RequestParam ("file") MultipartFile file, @RequestParam("name") String name,
                                    @RequestParam("description") String description, @RequestParam("visibilityId") int visibilityId,
                                    @RequestParam("categoryId") int categoryId, HttpSession s){
        //todo fix temp file bug
        System.out.println("Start uploading");
        int userId=getLoggedId(s);
        return videoService.uploadVideo(file,name,description,visibilityId,categoryId, userId);
    }

    @PostMapping("/videos/search")
    public List<SearchVideoDTO> searchVideo(@RequestBody String name){
        return videoService.searchVideo(name);
    }

    @PostMapping("/videos/{id}/reaction")
    public VideoReactionDTO reactToVideo(@PathVariable ("id") int videoId, @RequestBody Integer reaction, HttpSession s){
        int userId=getLoggedId(s);
        return videoService.reactToVideo(userId, videoId, reaction);
    }

    @PutMapping("/videos/{id}")
    public VideoInfoDTO editVideo(@PathVariable ("id") int videoId, @RequestBody EditVideoDTO editData, HttpSession s){
        int userId=getLoggedId(s);
        return videoService.editVideo(userId, videoId, editData);
    }

    @DeleteMapping("/videos/{id}")
    public void deleteVideo(@PathVariable ("id") int videoId, HttpSession s){
        int userId=getLoggedId(s);
        videoService.deleteVideo(userId, videoId);
    }

    @SneakyThrows
    @GetMapping("/videos/download/{fileName}")
    public void downloadVideo(@PathVariable("fileName") String fileName, HttpServletResponse resp ){
        File f = videoService.download(fileName);
        Files.copy(f.toPath(), resp.getOutputStream());
    }


}
