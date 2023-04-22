package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.model.exceptions.UnauthorizedException;
import com.youtube.youtube.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlaylistController extends AbstractController{
    @Autowired
    private PlaylistService playlistService;
    @GetMapping("/users/{id}/playlists")
    public UserPlaylistsDTO getUserPlaylists(@PathVariable int id){
        return playlistService.getUserPlaylists(id);
    }

    @PostMapping("/playlists")
    public PlaylistInfoDTO createPlaylist(@Valid @RequestBody CreatePlaylistDTO createData, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        return playlistService.createPlaylist(loggedId, createData);
    }
    @PostMapping("/playlists/search")
    public Page<SearchPlayListDTO> searchPlaylist(@RequestBody PlaylistInfoDTO searchData,  @RequestParam (defaultValue = "0") int page,
                                                  @RequestParam (defaultValue = "10") int size){
        return playlistService.searchPlaylist(searchData, page, size);
    }

    @PutMapping("/playlists/{id}/videos")
    public ResponseEntity<String> addVideoToPlaylist(@PathVariable ("id") int playlistId,@RequestBody int videoId,
                                                     @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        String result=playlistService.addVideoToPlaylist(loggedId, playlistId, videoId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/playlists/{id}")
    public PlaylistInfoDTO editPlaylist(@PathVariable ("id") int playlistId, @RequestBody CreatePlaylistDTO editData,
                                        @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        return playlistService.editPlaylist(loggedId, playlistId, editData);
    }

    @DeleteMapping("/playlists/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable ("id") int playlistId, @RequestHeader("Authorization") String authHeader){
        int loggedId = getUserId(authHeader);
        playlistService.deletePlaylist(loggedId, playlistId);
        return ResponseEntity.ok("Playlist deleted successfully.");
    }

    @PostMapping("/playlists/{id}")
    public Page<VideoWithoutOwnerDTO> getPlaylistById(@PathVariable int id, @RequestBody SortPlaylistDTO sortData,
                                                      @RequestParam (defaultValue = "0") int page,
                                                      @RequestParam (defaultValue = "10") int size){
        return playlistService.getPlaylistById( id, sortData, page, size);
    }

}
