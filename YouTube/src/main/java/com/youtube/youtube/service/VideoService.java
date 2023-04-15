package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.SearchVideoDTO;
import com.youtube.youtube.model.DTOs.UploadVideoDTO;
import com.youtube.youtube.model.DTOs.UserVideosDTO;
import com.youtube.youtube.model.DTOs.VideoInfoDTO;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.entities.Video;
import com.youtube.youtube.model.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VideoService extends AbstractService {

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

    public VideoInfoDTO uploadVideo(UploadVideoDTO uploadData, int userId) {
        //todo
        return null;
    }
}
