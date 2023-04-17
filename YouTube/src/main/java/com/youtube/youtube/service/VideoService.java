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
    @Autowired
    private VisibilityRepository visibilityRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    public VideoInfoDTO getVideoById(int id) {
        Video video = findVideoById(id);
        //TODO check video mapping
        return mapper.map(video,VideoInfoDTO.class);
    }

    public List<SearchVideoDTO> searchVideo(String name) {
        List<Video> videos = videoRepository.findAllByTitle(name);
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

    public UserVideosDTO getUserVideos(int id) {
        User user=getUserById(id);
        if(user.getVideos().isEmpty()){
            throw new NotFoundException("This user has no videos.");
        }
        return mapper.map(user, UserVideosDTO.class);
    }


    @SneakyThrows
    public VideoInfoDTO uploadVideo(MultipartFile file, String name, String description,int visibilityId, int categoryId, int userId) {
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
        //todo validate reaction
        Video video=findVideoById(videoId);
        User user=getUserById(userId);
        UserReactToVideo userReactToVideo= new UserReactToVideo(videoId,userId);

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

    private Visibility findVisibility(int visibilityId){
        Optional<Visibility> opt =visibilityRepository.findById(visibilityId);
        if(opt.isEmpty()){
            throw new NotFoundException("There is no such visibility option.");
        }
        return opt.get();
    }
    private Category findCategory(int categoryId){
        Optional<Category> opt =categoryRepository.findById(categoryId);
        if(opt.isEmpty()){
            throw new NotFoundException("There is no such category option.");
        }
        return opt.get();
    }


}
