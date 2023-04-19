package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.entities.*;
import com.youtube.youtube.model.exceptions.BadRequestException;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.repositories.VideoReactionRepository;
import com.youtube.youtube.model.repositories.VideoWatchRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VideoService extends AbstractService {
    @Autowired
    private VideoReactionRepository reactionRepository;
    @Autowired
    private VideoWatchRepository videoWatchRepository;
    private static String[] allowedVideoFormats = {"video/mp4", "video/mpeg", "video/webm",
                        "video/quicktime", "video/x-msvideo", "video/x-flv", "video/3gpp"};
    @Transactional
    public VideoInfoDTO getVideoById(int videoId, int userId) {
        Video video = findVideoById(videoId);
        if(videoWatchRepository.findById(userId, videoId).isEmpty()) {
            video.setViews(video.getViews() + 1);
            videoRepository.save(video);
        }
        videoWatchRepository.save(userId, videoId);
        return mapper.map(video,VideoInfoDTO.class);
    }

    public List<SearchVideoDTO> searchVideo(VideoWithoutOwnerDTO searchData) {
        List<Video> videos = videoRepository.findAllByName(searchData.getName());
        if(videos.isEmpty()){
            throw new NotFoundException("There is no video with searched name.");
        }
        return videos.stream()
                .map(v -> mapper.map(v, SearchVideoDTO.class))
                .collect(Collectors.toList());
    }

    public VideoInfoDTO editVideo(int userId, int videoId, EditVideoDTO editData) {
        Video video=findVideoById(videoId);
        checkVideoOwner(video,userId);
        video.setName(editData.getName());
        video.setDescription(editData.getDescription());
        video.setVisibility(findVisibility(editData.getVisibilityId()));
        video.setCategory(findCategory(editData.getCategoryId()));
        videoRepository.save(video);
        return mapper.map(video, VideoInfoDTO.class);
    }

    public ResponseEntity<String> deleteVideo(int userId, int videoId) {
        Video video=findVideoById(videoId);
        checkVideoOwner(video,userId);
        videoRepository.delete(video);
        return ResponseEntity.ok("Video deleted successfully.");
    }

    public UserVideosDTO getUserVideos(int userId) {
        User user=getUserById(userId);
        if(user.getVideos().isEmpty()){
            throw new NotFoundException("This user has no videos.");
        }
        return mapper.map(user, UserVideosDTO.class);
    }



    @SneakyThrows
    public VideoInfoDTO uploadVideo(MultipartFile file, String name, String description,
                                    int visibilityId, int categoryId, int userId) {
        validUploadData(file, name, description);
        Visibility visibility=findVisibility(visibilityId);
        Category category= findCategory(categoryId);
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + "."+ext;
        File dir = new File(UPLOADS);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File f = new File(dir, fileName);
        Files.copy(file.getInputStream(), f.toPath());
        String url = dir.getName() + File.separator + f.getName();
        User user = getUserById(userId);
        Video video=new Video();
        video.setName(name);
        video.setDescription(description);
        video.setVisibility(visibility);
        video.setCategory(category);
        video.setUser(user);
        video.setDateCreated(LocalDateTime.now());
        video.setVideoUrl(url);
        videoRepository.save(video);
        return mapper.map(video, VideoInfoDTO.class);
    }

    @Transactional
    public VideoReactionDTO reactToVideo(int userId, int videoId, int reaction) {
        validReaction(reaction);
        Video video=findVideoById(videoId);

        Optional<VideoReaction> existingReaction=reactionRepository.findById(userId, videoId);
        if(existingReaction.isPresent() && existingReaction.get().getReaction()==reaction){
            reactionRepository.delete(userId,videoId);
        }
        else{
            reactionRepository.save(userId, videoId, reaction);
        }

        int likes=reactionRepository.countByVideoIdAndReaction(videoId, LIKE);
        int dislikes=reactionRepository.countByVideoIdAndReaction(videoId, DISLIKE);
        VideoReactionDTO updatedVideo=mapper.map(video,VideoReactionDTO.class);
        updatedVideo.setLikes(likes);
        updatedVideo.setDislikes(dislikes);
        return updatedVideo;
    }

    public File download(String fileName) {
        File dir = new File(UPLOADS);
        File f = new File(dir, fileName);
        if(f.exists()){
            return f;
        }
        throw new NotFoundException("File not found.");
    }


    private void validUploadData(MultipartFile file, String name, String description){
        if (file.isEmpty()) {
            throw new BadRequestException("The file is not attached. Please try again.");
        }
        System.out.println(file.getContentType());
        if (!Arrays.asList(allowedVideoFormats).contains(file.getContentType())) {
            throw new BadRequestException("Invalid file format. Only video files are allowed.");
        }
        if (file.getSize() > MAX_VIDEO_SIZE) {
            throw new BadRequestException("File is too large. Maximum file size is 256 GB.");
        }
        if(name.length()> MAX_TITLE_LENGTH){
            throw new BadRequestException(TITLE_TOO_LONG);
        }
        if(description.length()> MAX_DESCRIPTION_LENGTH){
            throw new BadRequestException(DESCRIPTION_TOO_LONG);
        }
    }
}
