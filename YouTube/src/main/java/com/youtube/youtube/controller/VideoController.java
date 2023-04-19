package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import com.youtube.youtube.service.VideoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public VideoInfoDTO getVideoById(@PathVariable ("id") int videoId, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        if(loggedId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        return videoService.getVideoById(videoId, loggedId);
    }

    @PostMapping("/videos")
    public VideoInfoDTO uploadVideo(@RequestParam ("file") MultipartFile file, @RequestParam("name") String name,
                                    @RequestParam("description") String description, @RequestParam("visibilityId") int visibilityId,
                                    @RequestParam("categoryId") int categoryId, @RequestHeader("Authorization") String authHeader){
        //todo fix temp file bug
        System.out.println("Start uploading");
        int loggedId = getUserId(authHeader);
        if(loggedId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        return videoService.uploadVideo(file,name,description,visibilityId,categoryId, loggedId);
    }

    @PostMapping("/videos/search")
    public List<SearchVideoDTO> searchVideo(@RequestBody VideoWithoutOwnerDTO searchData){
        return videoService.searchVideo(searchData);
    }

    @PostMapping("/videos/{id}/reaction")
    public VideoReactionDTO reactToVideo(@PathVariable ("id") int videoId, @RequestBody Integer reaction, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        if(loggedId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        return videoService.reactToVideo(loggedId, videoId, reaction);
    }

    @PutMapping("/videos/{id}")
    public VideoInfoDTO editVideo(@PathVariable ("id") int videoId, @Valid @RequestBody EditVideoDTO editData, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        if(loggedId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        return videoService.editVideo(loggedId, videoId, editData);
    }

    @DeleteMapping("/videos/{id}")
    public ResponseEntity<String> deleteVideo(@PathVariable ("id") int videoId, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        if(loggedId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        return videoService.deleteVideo(loggedId, videoId);
    }

    @SneakyThrows
    @GetMapping("/videos/download/{fileName}")
    public void downloadVideo(@PathVariable("fileName") String fileName, HttpServletResponse resp ){
        File f = videoService.download(fileName);
        Files.copy(f.toPath(), resp.getOutputStream());
    }

}
