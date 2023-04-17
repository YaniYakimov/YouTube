package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.CreatePlaylistDTO;
import com.youtube.youtube.model.DTOs.PlaylistInfoDTO;
import com.youtube.youtube.model.DTOs.PlaylistWithoutOwnerDTO;
import com.youtube.youtube.model.DTOs.UserPlaylistsDTO;
import com.youtube.youtube.service.PlaylistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PlaylistInfoDTO createPlaylist(@RequestBody CreatePlaylistDTO createData, HttpSession s){
        int userId=getLoggedId(s);
        return playlistService.createPlaylist(userId, createData);
    }
    @PostMapping("/playlists/search")
    public PlaylistWithoutOwnerDTO searchPlaylist(@RequestBody String name){
        // todo return playlistService.searchPlaylist(name);
        return null;
    }

    @PutMapping("/playlists/{id}/videos")
    public String addVideoToPlaylist(@PathVariable ("id") int playlistId,@RequestBody int videoId, HttpSession s ){
        int userId=getLoggedId(s);
        return playlistService.addVideoToPlaylist(userId, playlistId, videoId);
    }

    @PutMapping("/playlists/{id}")
    public PlaylistInfoDTO editPlaylist(@RequestParam ("id") int playlistId, @RequestBody CreatePlaylistDTO editData, HttpSession s){
        int userId=getLoggedId(s);
        // todo return playlistService.editPlaylist(userId, playlistId, editData);
        return null;
    }

    @DeleteMapping("/playlists/{id}")
    public void deletePlaylist(@RequestParam ("id") int playlistId, HttpSession s){
        int userId=getLoggedId(s);
        //todo playlistService.deletePlaylist(userId, playlistId);

    }

}
