package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import com.youtube.youtube.service.AmazonService;
import com.youtube.youtube.service.VideoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class VideoController extends AbstractController{
    @Autowired
    private VideoService videoService;
    @Autowired
    private AmazonService amazonService;
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

    @PostMapping("/videos/upload-s3")
    public VideoInfoDTO uploadVideoAWS(@RequestParam ("file") MultipartFile file, @RequestParam("name") String name,
                                       @RequestParam("description") String description, @RequestParam("visibilityId") int visibilityId,
                                       @RequestParam("categoryId") int categoryId, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        if(loggedId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        return amazonService.uploadFile(file,name,description,visibilityId,categoryId,loggedId);
    }

    @GetMapping("/videos/download")
    public void downloadVideoAWS(@RequestParam String url, HttpServletResponse resp ){
        //todo logged
        try {
            InputStream inputStream = amazonService.download(url);
            IOUtils.copy(inputStream, resp.getOutputStream());
            resp.flushBuffer();
        }catch (IOException e){
            throw new RuntimeException("Download unsuccessful");
        }
    }

}
