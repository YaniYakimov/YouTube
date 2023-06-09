package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import com.youtube.youtube.service.AmazonService;
import com.youtube.youtube.service.VideoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class VideoController extends AbstractController{
    @Autowired
    private VideoService videoService;
    @Autowired
    private AmazonService amazonService;

    @GetMapping("/users/{id}/videos")
    public Page<VideoWithoutOwnerDTO> getUserVideos(@PathVariable int id, @RequestParam (defaultValue = "0") int page,
                                                    @RequestParam (defaultValue = "10") int size){
        return videoService.getUserVideos(id, page, size);
    }
    @GetMapping("/videos/{id}")
    public VideoInfoDTO getVideoById(@PathVariable ("id") int videoId, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        return videoService.getVideoById(videoId, loggedId);
    }

    @PostMapping("/videos/search")
    public Page<SearchVideoDTO> searchVideo(@RequestBody VideoWithoutOwnerDTO searchData, @RequestParam (defaultValue = "0") int page,
                                            @RequestParam (defaultValue = "10") int size){
        return videoService.searchVideo(searchData, page, size);
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
        return videoService.editVideo(loggedId, videoId, editData);
    }

    @DeleteMapping("/videos/{id}")
    public ResponseEntity<String> deleteVideo(@PathVariable ("id") int videoId, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        amazonService.deleteVideo(loggedId, videoId);
        return ResponseEntity.ok("Video deleted successfully.");
    }

    @PostMapping("/videos/upload")
    public VideoInfoDTO uploadVideoAWS(@RequestParam ("file") MultipartFile file, @RequestParam("name") String name,
                                       @RequestParam("description") String description, @RequestParam("visibilityId") int visibilityId,
                                       @RequestParam("categoryId") int categoryId, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        return amazonService.uploadFile(file,name,description,visibilityId,categoryId,loggedId);
    }

    @GetMapping("/videos/download")
    public void downloadVideoAWS(@RequestParam String url, HttpServletResponse resp, @RequestHeader("Authorization") String authHeader ){
        getUserId(authHeader);
        try {
            InputStream inputStream = amazonService.download(url);
            IOUtils.copy(inputStream, resp.getOutputStream());
            resp.flushBuffer();
        }catch (IOException e){
            throw new RuntimeException("Download unsuccessful");
        }
    }

}
