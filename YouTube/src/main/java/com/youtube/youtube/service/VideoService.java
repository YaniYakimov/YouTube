package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.entities.*;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.repositories.VideoReactionRepository;
import com.youtube.youtube.model.repositories.VideoWatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class VideoService extends AbstractService {
    @Autowired
    private VideoReactionRepository reactionRepository;
    @Autowired
    private VideoWatchRepository videoWatchRepository;
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

    public Page<SearchVideoDTO> searchVideo(VideoWithoutOwnerDTO searchData, int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"views"));
        Page<SearchVideoDTO> videos = videoRepository.findAllByTitle(searchData.getName(), pageable)
                .map(v -> mapper.map(v, SearchVideoDTO.class));
        if(videos.isEmpty()){
            throw new NotFoundException("There is no video with searched name.");
        }
        return videos;
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

    public Page<VideoWithoutOwnerDTO> getUserVideos(int userId, int page, int size) {
        User user=getUserById(userId);
        if(user.getVideos().isEmpty()){
            throw new NotFoundException("This user has no videos.");
        }
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.ASC, "name"));
        return videoRepository.findAllByUser(userId, pageable)
                .map(video -> mapper.map(video, VideoWithoutOwnerDTO.class));

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
}
