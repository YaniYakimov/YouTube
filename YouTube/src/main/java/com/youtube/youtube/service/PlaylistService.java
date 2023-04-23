package com.youtube.youtube.service;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.entities.Playlist;
import com.youtube.youtube.model.entities.User;
import com.youtube.youtube.model.entities.Video;
import com.youtube.youtube.model.entities.Visibility;
import com.youtube.youtube.model.exceptions.BadRequestException;
import com.youtube.youtube.model.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PlaylistService extends AbstractService {
    private enum SortType {VIEWS,NAME,DATE_CREATED}

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

    public Page<SearchPlayListDTO> searchPlaylist(PlaylistInfoDTO searchData, int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"name"));
        Page<SearchPlayListDTO> playlist = playlistRepository.findAllByTitle(searchData.getName(), pageable)
                .map(v -> mapper.map(v, SearchPlayListDTO.class));
        if(playlist.isEmpty()) {
            throw new NotFoundException("There is no playlist with searched name.");
        }
        return playlist;
    }
@Transactional
    public void deletePlaylist(int userId, int playlistId) {
        Playlist playlist=findPlaylistById(playlistId);
        checkPlaylistOwner(playlist, userId);
        playlistRepository.delete(playlist);
    }
    public Page<VideoWithoutOwnerDTO> getPlaylistById(int id, SortPlaylistDTO sortData, int page, int size) {
        getSortType(sortData.getSortType());
        Playlist playlist=findPlaylistById(id);
        playlist.setViews(playlist.getViews()+1);
        playlistRepository.save(playlist);

        Sort.Direction direction= sortData.isDescOrder() ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable=PageRequest.of(page,size, Sort.by(direction, sortData.getSortType()));
        return videoRepository.findAllInPlaylist(id, pageable)
                .map(video -> mapper.map(video, VideoWithoutOwnerDTO.class));
    }


    private boolean getSortType(String orderBy){
        for (int i = 0; i < SortType.values().length; i++) {
            if(orderBy.equalsIgnoreCase(SortType.values()[i].toString())){
                return true;
            }
        }
        throw new BadRequestException("Invalid sort type");
    }
}
