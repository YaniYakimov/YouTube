package com.youtube.youtube.service;

import com.youtube.youtube.model.entities.Category;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.entities.Video;
import com.youtube.youtube.model.entities.Visibility;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import com.youtube.youtube.model.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public abstract class AbstractService {
    public static final int LIKE = 1;
    public static final int DISLIKE = -1;
    @Autowired
    protected VideoRepository videoRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected LocationRepository locationRepository;
    @Autowired
    protected VisibilityRepository visibilityRepository;
    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected ModelMapper mapper;
    protected User getUserById(int id) {
        return  userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }
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

    protected boolean validReaction(int reaction){
        switch (reaction){
            case LIKE:
            case DISLIKE:
                return true;
            default:
                throw new NotFoundException("There is no such reaction");
        }
    }

    protected Visibility findVisibility(int visibilityId){
        Optional<Visibility> opt =visibilityRepository.findById(visibilityId);
        if(opt.isEmpty()){
            throw new NotFoundException("There is no such visibility option.");
        }
        return opt.get();
    }
    protected Category findCategory(int categoryId){
        Optional<Category> opt =categoryRepository.findById(categoryId);
        if(opt.isEmpty()){
            throw new NotFoundException("There is no such category option.");
        }
        return opt.get();
    }
}
