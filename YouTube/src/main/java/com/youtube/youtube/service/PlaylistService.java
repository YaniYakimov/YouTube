package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.CreatePlaylistDTO;
import com.youtube.youtube.model.DTOs.PlaylistInfoDTO;
import com.youtube.youtube.model.entities.Playlist;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.entities.Visibility;
import com.youtube.youtube.model.repositories.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService extends AbstractService {
    @Autowired
    PlaylistRepository playlistRepository;
    public PlaylistInfoDTO createPlaylist(int userId, CreatePlaylistDTO createData) {
        Playlist playlist = new Playlist();
//        mapper.map(createData, playlist);
        playlist.setName(createData.getName());
        playlist.setDescription(createData.getDescription());
        User user = getUserById(userId);
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
}
