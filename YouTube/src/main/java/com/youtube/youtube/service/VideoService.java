package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.SearchVideoDTO;
import com.youtube.youtube.model.DTOs.VideoInfoDTO;
import com.youtube.youtube.model.entities.Video;
import com.youtube.youtube.model.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoService extends AbstractService {

    public VideoInfoDTO getVideoById(int id) {
        Video video = findVideoById(id);
        //TODO map video -> VideoInfoDTO
        return null;
    }

    public SearchVideoDTO searchVideo(String name) {
        Optional<Video> opt = videoRepository.findByName(name);
        if(opt.isEmpty()){
            throw new NotFoundException("Video not found.");
        }
        Video video = opt.get();
        //TODO map video -> SearchVideoDTO
        return null;
    }

    public VideoInfoDTO editVideo(int userId, int videoId, VideoInfoDTO editData) {
        Video video=findVideoById(videoId);

        checkVideoOwner(video,userId);
        //TODO edit video
        //map video
        return null;
    }

    public void deleteVideo(int userId, int videoId) {
        Video video=findVideoById(videoId);
        checkVideoOwner(video,userId);
        //TODO delete video
        //map video

    }
}
