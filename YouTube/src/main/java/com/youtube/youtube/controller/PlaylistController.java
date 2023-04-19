package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import com.youtube.youtube.service.PlaylistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlaylistController extends AbstractController{
    @Autowired
    private PlaylistService playlistService;
    @GetMapping("/users/{id}/playlists")
    public UserPlaylistsDTO getUserPlaylists(@PathVariable int id){
        return playlistService.getUserPlaylists(id);
    }
    @PostMapping("/playlists")
    public PlaylistInfoDTO createPlaylist(@RequestBody CreatePlaylistDTO createData, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        if(loggedId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        return playlistService.createPlaylist(loggedId, createData);
    }
    @PostMapping("/playlists/search")
    public List<SearchPlayListDTO> searchPlaylist(@RequestBody CreatePlaylistDTO searchData){
        return playlistService.searchPlaylist(searchData);
    }

    @PutMapping("/playlists/{id}/videos")
    public ResponseEntity<String> addVideoToPlaylist(@PathVariable ("id") int playlistId,@RequestBody int videoId, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        if(loggedId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        String result=playlistService.addVideoToPlaylist(loggedId, playlistId, videoId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/playlists/{id}")
    public PlaylistInfoDTO editPlaylist(@PathVariable ("id") int playlistId, @RequestBody CreatePlaylistDTO editData, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        if(loggedId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        return playlistService.editPlaylist(loggedId, playlistId, editData);
    }

    @DeleteMapping("/playlists/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable ("id") int playlistId, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        if(loggedId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        playlistService.deletePlaylist(loggedId, playlistId);
        return ResponseEntity.ok("Playlist deleted successfully.");

    }

    @PostMapping("/playlists/{id}")
    public PlaylistSortDTO getPlaylistById(@PathVariable int id, @RequestBody SortPlaylistDTO sortData, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        if(loggedId == 0) {
            throw new UnauthorizedException(YOU_HAVE_TO_LOG_IN_FIRST);
        }
        return playlistService.getPlaylistById(loggedId, id, sortData);
    }

}
