package com.youtube.youtube.service;

import com.youtube.youtube.model.entities.Video;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import com.youtube.youtube.model.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public abstract class AbstractService {
    @Autowired
    protected VideoRepository videoRepository;



    protected Video findVideoById(int id){
        Optional<Video> opt = videoRepository.findById(id);
        if(opt.isEmpty()){
            throw new NotFoundException("Video not found.");
        }
        return opt.get();
    }

    protected boolean checkVideoOwner(Video video, int userId){
        if(video.getUser().getId() != userId){
            throw new UnauthorizedException("Cannot access this.");
        }
        return true;
    }
}
