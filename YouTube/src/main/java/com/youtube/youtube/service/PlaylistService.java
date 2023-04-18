package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.entities.Playlist;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.entities.Video;
import com.youtube.youtube.model.entities.Visibility;
import com.youtube.youtube.model.exceptions.BadRequestException;
import com.youtube.youtube.model.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        playlist.setDateCreated(LocalDateTime.now());
        playlistRepository.save(playlist);
        return mapper.map(playlist, PlaylistInfoDTO.class);

    }

    public UserPlaylistsDTO getUserPlaylists(int userId) {
        User user=getUserById(userId);
        if(user.getPlaylists().isEmpty()){
            throw new NotFoundException("This user has no playlists.");
        }

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

    public PlaylistInfoDTO editPlaylist(int userId, int playlistId, CreatePlaylistDTO editData) {
        Playlist playlist=findPlaylistById(playlistId);
        checkPlaylistOwner(playlist, userId);
        playlist.setName(editData.getName());
        playlist.setDescription(editData.getDescription());

        Visibility visibility= findVisibility(editData.getVisibilityId());
        playlist.setVisibility(visibility);
        playlistRepository.save(playlist);
        return mapper.map(playlist, PlaylistInfoDTO.class);
    }

    public List<SearchPlayListDTO> searchPlaylist(CreatePlaylistDTO searchData) {
        List<Playlist> result = playlistRepository.findAllByName(searchData.getName());
        if(result.isEmpty()) {
            throw new NotFoundException("There is no playlist with searched name.");
        }
        return  result.stream()
                .map(p -> mapper.map(p, SearchPlayListDTO.class))
                .collect(Collectors.toList());
    }

    public void deletePlaylist(int userId, int playlistId) {
        Playlist playlist=findPlaylistById(playlistId);
        checkPlaylistOwner(playlist, userId);
        playlistRepository.delete(playlist);
    }

    public List<VideoReactionDTO> sortPlaylist(int userId, int id, SortPlaylistDTO sortData) {
        Playlist playlist=findPlaylistById(id);
        checkPlaylistOwner(playlist, userId);
        List<Video> videos=playlist.getVideos().stream().toList();
        //todo validate sortData
        String orderBy=sortData.getOrderBy();

        switch (orderBy){
            case "VIEWS":
                Collections.sort(videos, orderByViews(videos));
                break;
            case "NAME":
                Collections.sort(videos,orderByName(videos));
                break;
        }
        if(sortData.getOrder().equals("DESCENDING")){
            //todo
        }
        return null;
    }

    private Comparator<Video> orderByViews(List <Video> videos){
        return Comparator.comparing(video -> video.getViews());
    }
    private Comparator<Video> orderByName(List <Video> videos){
        return Comparator.comparing(video -> video.getName());
    }

}
