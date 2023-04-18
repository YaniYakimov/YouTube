package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
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
    public PlaylistInfoDTO createPlaylist(@RequestBody CreatePlaylistDTO createData, HttpSession s){
        int userId=getLoggedId(s);
        return playlistService.createPlaylist(userId, createData);
    }
    @PostMapping("/playlists/search")
    public List<SearchPlayListDTO> searchPlaylist(@RequestBody CreatePlaylistDTO searchData){
        return playlistService.searchPlaylist(searchData);
    }

    @PutMapping("/playlists/{id}/videos")
    public ResponseEntity<String> addVideoToPlaylist(@PathVariable ("id") int playlistId,@RequestBody int videoId, HttpSession s ){
        int userId=getLoggedId(s);
        String result=playlistService.addVideoToPlaylist(userId, playlistId, videoId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/playlists/{id}")
    public PlaylistInfoDTO editPlaylist(@PathVariable ("id") int playlistId, @RequestBody CreatePlaylistDTO editData, HttpSession s){
        int userId=getLoggedId(s);
        return playlistService.editPlaylist(userId, playlistId, editData);
    }

    @DeleteMapping("/playlists/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable ("id") int playlistId, HttpSession s){
        int userId=getLoggedId(s);
        playlistService.deletePlaylist(userId, playlistId);
        return ResponseEntity.ok("Playlist deleted successfully.");

    }

    @PostMapping("/playlists/{id}")
    public PlaylistSortDTO getPlaylistById(@PathVariable int id, @RequestBody SortPlaylistDTO sortData, HttpSession s){
        int userId=getLoggedId(s);
        return playlistService.getPlaylistById(userId, id, sortData);
    }

}
