package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.entities.*;
import com.youtube.youtube.model.exceptions.BadRequestException;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.repositories.VideoReactionRepository;
import com.youtube.youtube.model.repositories.VideoWatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        List<Video> videos = videoRepository.findAllByTitle(searchData.getName());
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
