package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.entities.Playlist;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.entities.Video;
import com.youtube.youtube.model.entities.Visibility;
import com.youtube.youtube.model.exceptions.BadRequestException;
import com.youtube.youtube.model.exceptions.NotFoundException;
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

    public List<SearchPlayListDTO> searchPlaylist(PlaylistInfoDTO searchData) {
        List<Playlist> result = playlistRepository.findAllByTitle(searchData.getName());
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

    public PlaylistSortDTO getPlaylistById(int userId, int id, SortPlaylistDTO sortData) {
        Playlist playlist=findPlaylistById(id);
        checkPlaylistOwner(playlist, userId);
        playlist.setViews(playlist.getViews()+1);
        playlistRepository.save(playlist);
        List<Video> videos=playlist.getVideos().stream().toList();
        SortType sortType=getSortType(sortData.getSortType());

        List<VideoWithoutOwnerDTO> videosDTO= videos.stream()
                .map( v -> mapper.map(v, VideoWithoutOwnerDTO.class))
                .collect(Collectors.toList());

        switch (sortType){
            case VIEWS -> {
                videosDTO.sort((v1, v2) -> Long.compare(v1.getViews(), v2.getViews()));
            }
            case NAME -> {
                videosDTO.sort((v1, v2) -> v1.getName().compareTo(v2.getName()));
            }
            case DATE -> {
                videosDTO.sort((v1, v2) -> v1.getDateCreated().compareTo(v2.getDateCreated()));
            }
        }
        if(sortData.isDescOrder()){
            Collections.reverse(videosDTO);
        }

        PlaylistSortDTO result=mapper.map(playlist, PlaylistSortDTO.class);
        result.setSortedVideos(videosDTO);
        return result;
    }

    private enum SortType {VIEWS,NAME,DATE}

    private SortType getSortType(String orderBy){
        for (int i = 0; i < SortType.values().length; i++) {
            if(orderBy.equalsIgnoreCase(SortType.values()[i].toString())){
                return SortType.values()[i];
            }
        }
        throw new BadRequestException("Invalid sort type");
    }

}
