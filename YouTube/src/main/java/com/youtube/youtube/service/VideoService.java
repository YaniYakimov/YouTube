package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.entities.*;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.repositories.CategoryRepository;
import com.youtube.youtube.model.repositories.VideoReactionRepository;
import com.youtube.youtube.model.repositories.VisibilityRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VideoService extends AbstractService {
    @Autowired
    private VideoReactionRepository reactionRepository;

    public VideoInfoDTO getVideoById(int id) {
        Video video = findVideoById(id);
        return mapper.map(video,VideoInfoDTO.class);
    }

    public List<SearchVideoDTO> searchVideo(VideoWithoutOwnerDTO searchData) {
        List<Video> videos = videoRepository.findAllByName(searchData.getName());
        if(videos.isEmpty()){
            throw new NotFoundException("There is no video with searched name.");
        }
        //TODO check video mapping
        return videos.stream()
                .map(v -> mapper.map(v, SearchVideoDTO.class))
                .collect(Collectors.toList());
    }

    public VideoInfoDTO editVideo(int userId, int videoId, EditVideoDTO editData) {
        Video video=findVideoById(videoId);

        checkVideoOwner(video,userId);
        //TODO check edit data
//        mapper.map(editData, video);
        video.setName(editData.getName());
        video.setDescription(editData.getDescription());
        video.setVisibility(findVisibility(editData.getVisibilityId()));
        video.setCategory(findCategory(editData.getCategoryId()));

        //TODO check video mapping
        return mapper.map(video, VideoInfoDTO.class);
    }

    public void deleteVideo(int userId, int videoId) {
        Video video=findVideoById(videoId);
        checkVideoOwner(video,userId);
        videoRepository.deleteById(videoId);

    }

    public UserVideosDTO getUserVideos(int userId) {
        User user=getUserById(userId);
        if(user.getVideos().isEmpty()){
            throw new NotFoundException("This user has no videos.");
        }
        return mapper.map(user, UserVideosDTO.class);
    }



    @SneakyThrows
    public VideoInfoDTO uploadVideo(MultipartFile file, String name, String description, int visibilityId, int categoryId, int userId) {
        //todo validate info
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + "."+ext;
        File dir = new File("uploads");
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

        Visibility visibility=findVisibility(visibilityId);
        video.setVisibility(visibility);

        Category category= findCategory(categoryId);
        video.setCategory(category);

        video.setUser(user);
        video.setDateCreated(LocalDateTime.now());
        video.setVideoUrl(url);

        user.getVideos().add(video);
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
        File dir = new File("uploads");
        File f = new File(dir, fileName);
        if(f.exists()){
            return f;
        }
        throw new NotFoundException("File not found.");
    }







}
