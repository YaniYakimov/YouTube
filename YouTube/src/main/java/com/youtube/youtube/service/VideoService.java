package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.SearchVideoDTO;
import com.youtube.youtube.model.DTOs.UploadVideoDTO;
import com.youtube.youtube.model.DTOs.UserVideosDTO;
import com.youtube.youtube.model.DTOs.VideoInfoDTO;
import com.youtube.youtube.model.entities.Category;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.entities.Video;
import com.youtube.youtube.model.entities.Visibility;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.repositories.CategoryRepository;
import com.youtube.youtube.model.repositories.VisibilityRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VideoService extends AbstractService {
    @Autowired
    private VisibilityRepository visibilityRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    public VideoInfoDTO getVideoById(int id) {
        Video video = findVideoById(id);
        //TODO check video mapping
        return mapper.map(video,VideoInfoDTO.class);
    }

    public Set<SearchVideoDTO> searchVideo(String name) {
        Set<Video> videos = videoRepository.findAllByName(name);
        if(videos.isEmpty()){
            throw new NotFoundException("There is no video with searched name.");
        }
        //TODO check video mapping
        return videos.stream()
                .map(v -> mapper.map(v, SearchVideoDTO.class))
                .collect(Collectors.toSet());
    }

    public VideoInfoDTO editVideo(int userId, int videoId, VideoInfoDTO editData) {
        Video video=findVideoById(videoId);

        checkVideoOwner(video,userId);
        //TODO edit video
        mapper.map(editData, video);
        videoRepository.save(video);
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
        //todo check visibility
        Visibility visibility=visibilityRepository.findById(visibilityId).get();
        video.setVisibility(visibility);
        //todo check category
        Category category=categoryRepository.findById(categoryId).get();
        video.setCategory(category);

        video.setUser(user);
        video.setDateCreated(LocalDateTime.now());
        video.setVideoUrl(url);

        user.getVideos().add(video);
        videoRepository.save(video);
        return mapper.map(video, VideoInfoDTO.class);
    }
}
