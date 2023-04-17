package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.*;
import com.youtube.youtube.service.PlaylistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String addVideoToPlaylist(@PathVariable ("id") int playlistId,@RequestBody int videoId, HttpSession s ){
        int userId=getLoggedId(s);
        return playlistService.addVideoToPlaylist(userId, playlistId, videoId);
    }

    @PutMapping("/playlists/{id}")
    public PlaylistInfoDTO editPlaylist(@PathVariable ("id") int playlistId, @RequestBody CreatePlaylistDTO editData, HttpSession s){
        int userId=getLoggedId(s);
        return playlistService.editPlaylist(userId, playlistId, editData);
    }

    @DeleteMapping("/playlists/{id}")
    public void deletePlaylist(@PathVariable ("id") int playlistId, HttpSession s){
        int userId=getLoggedId(s);
        //todo playlistService.deletePlaylist(userId, playlistId);

    }

    //todo playlistSort
}
