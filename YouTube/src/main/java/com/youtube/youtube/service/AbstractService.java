package com.youtube.youtube.service;

import com.youtube.youtube.model.entities.*;
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
    public static final String WRONG_CREDENTIALS = "Wrong credentials";
    public static final String NO_SUCH_USER = "No such user!";
    public static final String NO_SUCH_COUNTRY = "No such country!";
    public static final String NO_SUCH_COMMENT = "No such comment!";
    public static final String NO_SUCH_VIDEO = "No such video!";
    public static final String UPLOADS = "uploads";
    public static final long MAX_VIDEO_SIZE = 256L*1024*1024;
    public static final int MAX_TITLE_LENGTH =70;
    public static final int MAX_DESCRIPTION_LENGTH =5000;
    public static final String TITLE_TOO_LONG = "The title is too long. Maximum title length is "+MAX_TITLE_LENGTH+" characters.";
    public static final String DESCRIPTION_TOO_LONG = "The description is too long. Maximum description length is "+MAX_DESCRIPTION_LENGTH+" characters.";
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
    @Autowired
    protected PlaylistRepository playlistRepository;
    protected User getUserById(int id) {
        return  userRepository.findById(id).orElseThrow(() -> new NotFoundException(NO_SUCH_USER));
    }
    protected Video findVideoById(int id){
        Optional<Video> opt = videoRepository.findById(id);
        if(opt.isEmpty()){
            throw new NotFoundException(NO_SUCH_VIDEO);
        }
        return opt.get();
    }

    protected boolean checkVideoOwner(Video video, int userId){
        if(video.getUser().getId() != userId){
            throw new UnauthorizedException("You are not the owner of this video.");
        }
        return true;
    }

    protected boolean validReaction(int reaction){
        switch (reaction){
            case LIKE:
            case DISLIKE:
                return true;
            default:
                throw new NotFoundException("No such reaction!");
        }
    }

    protected Visibility findVisibility(int visibilityId){
        Optional<Visibility> opt =visibilityRepository.findById(visibilityId);
        if(opt.isEmpty()){
            throw new NotFoundException("No such visibility option!");
        }
        return opt.get();
    }
    protected Category findCategory(int categoryId){
        Optional<Category> opt =categoryRepository.findById(categoryId);
        if(opt.isEmpty()){
            throw new NotFoundException("No such category option!");
        }
        return opt.get();
    }

    protected boolean checkPlaylistOwner(Playlist playlist, int userId){
        if(playlist.getUser().getId() != userId){
            throw new UnauthorizedException("You are not the owner of this playlist.");
        }
        return true;
    }
    protected Playlist findPlaylistById(int id){
        Optional<Playlist> opt = playlistRepository.findById(id);
        if(opt.isEmpty()){
            throw new NotFoundException("Playlist not found.");
        }
        return opt.get();
    }
}
