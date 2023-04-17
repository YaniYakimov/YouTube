package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.entities.Playlist;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.entities.Video;
import com.youtube.youtube.model.entities.Visibility;
import com.youtube.youtube.model.exceptions.BadRequestException;
import com.youtube.youtube.model.exceptions.NotFoundException;
import com.youtube.youtube.model.repositories.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PlaylistService extends AbstractService {

    public PlaylistInfoDTO createPlaylist(int userId, CreatePlaylistDTO createData) {
        Playlist playlist = new Playlist();
        User user = getUserById(userId);
        //todo validate data
        if(playlistRepository.findByNameAndUser(createData.getName(), user).isPresent()){
            throw new BadRequestException("Playlist with this name already exists. Please choose another name.");
        }
        mapper.map(createData, playlist);

        Visibility visibility= findVisibility(createData.getVisibilityId());
        playlist.setUser(user);
        playlist.setVisibility(visibility);
        playlist.setViews(0);
        String playlistUrl = createData.getName().replaceAll("\\s+", "-").toLowerCase() + "-" + System.currentTimeMillis();
        playlist.setPlaylistUrl(playlistUrl);
//        user.getPlaylists().add(playlist);
        playlistRepository.save(playlist);
        return mapper.map(playlist, PlaylistInfoDTO.class);

    }

    public UserPlaylistsDTO getUserPlaylists(int userId) {
        User user=getUserById(userId);
        if(user.getPlaylists().isEmpty()){
            throw new NotFoundException("This user has no playlists.");
        }
//        return mapper.map(user, UserPlaylistsDTO.class);
        UserPlaylistsDTO userPlaylistsDTO= mapper.map(user, UserPlaylistsDTO.class);
        for(PlaylistWithoutOwnerDTO playlistDTO : userPlaylistsDTO.getPlaylists()){
            playlistDTO.setVideosCount(playlistRepository.countVideosInPlaylist(playlistDTO.getId()));
        }
        return userPlaylistsDTO;
    }

    public String addVideoToPlaylist(int userId, int playlistId, int videoId) {
        Playlist playlist=findPlaylistById(playlistId);
        Video video=findVideoById(videoId);
        checkPlaylistOwner(playlist, userId);
        playlist.getVideos().add(video);
        playlistRepository.save(playlist);
        return "Video added to playlist "+playlist.getName();
    }
}
